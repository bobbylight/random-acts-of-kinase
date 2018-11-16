<template>
    <div class="compound-network-wrapper">

        <loading-mask v-if="loading" class="compound-network-component"></loading-mask>
        <div v-show="!loading" ref="networkDiv" class="compound-network-component"></div>

        <v-menu
            v-model="showMenu"
            absolute
            :position-x="contextMenuX"
            :position-y="contextMenuY"
            offset-y
            style="max-width: 600px"
        >
            <v-list>
                <v-list-tile @click="">
                    <v-list-tile-title>Menu item 1</v-list-tile-title>
                </v-list-tile>
            </v-list>
        </v-menu>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import { Color, Data, DataSet, Edge, Network, Node } from 'vis';
import restApi from './rest-api';
import { ActivityProfile, Compound, ErrorResponse } from './rak';
import Toaster from './toaster';
import LoadingMask from './loading-mask.vue';

interface CompoundOrActivityProfileNode extends Node {
    data: {
        compound?: string;
        activityProfile?: ActivityProfile;
    };
}

interface CompoundNetworkEdge extends Edge {
    activityProfile?: ActivityProfile;
}

let network: Network | null = null;
let nodeDataSet: DataSet<CompoundOrActivityProfileNode> | null = null;
let edgeDataSet: DataSet<Edge> | null = null;

@Component({ components: { LoadingMask } })
export default class CompoundNetwork extends Vue {

    @Prop({ required: true })
    private compounds: Compound[];

    @Prop({ required: true })
    private physicsEnabled: boolean;

    @Prop({ required: true })
    private percentControl: string;

    private activityProfiles: ActivityProfile[];
    private loading: boolean = false;
    private showMenu: boolean = false;
    private contextMenuX: number = 0;
    private contextMenuY: number = 0;

    private compoundColor: Color = {
        border: '#bcbec0',
        background: '#f8f8f8',
        highlight: {
            border: '#d99cc5',
            background: '#ffdef4'
        }
    };

    private kinaseColor: Color = {
        border: '#bcbec0',
        background: '#f8f8f8',
        highlight: {
            border: '#9cc5d9',
            background: '#d3f4ff'
        }
    };

    mounted() {
        this.redraw();
    }

    private redraw() {

        if (!this.compounds || !this.compounds.length) {
            console.log('Not redrawing because no compounds were specified');
            return;
        }

        this.loading = true;

        const compoundNames: string[] = this.compounds.map((compound: Compound) => {
            return compound.compoundName;
        });

        restApi.getAllActivityProfiles(compoundNames)
            .then((activityProfiles: ActivityProfile[]) => {

                this.activityProfiles = activityProfiles;
                this.updateDataSets();

                const container: HTMLElement = this.$refs.networkDiv as HTMLElement;

                const data: Data = {
                    nodes: nodeDataSet!,
                    edges: edgeDataSet!
                };

                // Type definition for Options is missing nodes.margin and other valid properties
                const options: any /*Options*/ = {
                    nodes: {
                        shape: 'image',
                        shapeProperties: {
                            borderRadius: 2,
                            useBorderWithImage: true
                        },
                        size: 24,
                        borderWidth: 3,
                        margin: 80,
                        font: {
                            face: 'Roboto' // Match application font, visjs defaults to verdana
                        }
                    },
                    edges: {
                        color: {
                            color: '#a6a6a6',
                            inherit: false
                        },
                        font: {
                            face: 'Roboto' // Match application font, visjs defaults to verdana
                        }
                    },
                    layout: {
                        improvedLayout: true
                    },
                    interaction: {
                        multiselect: true
                    },
                    physics: {
                        enabled: this.physicsEnabled,
                        repulsion: {
                            nodeDistance: 200
                        }
                    }
                };

                this.loading = false;

                this.$nextTick(() => {

                    if (network) {
                        network.destroy();
                    }

                    network = new Network(container, data, options);

                    network.on('showPopup', (popupItemId: string) => {
                        console.log(`Show tool tip for item: ${popupItemId}`);
                    });

                    network.on('oncontext', (params: any) => {
                        params.event.preventDefault();
                        console.log(`Selected nodes: ${network!.getSelectedNodes()}, ` +
                            `selected edges: ${network!.getSelectedEdges()}`);
                        this.contextMenuX = params.event.clientX;
                        this.contextMenuY = params.event.clientY;
                        this.$nextTick(() => {
                            this.showMenu = true;
                        });
                    });
                });
            })
            .catch((e: ErrorResponse) => {
                this.loading = false;
                Toaster.error(e.message);
            });
    }

    @Watch('compounds')
    onCompoundUpdated() {
        this.redraw();
    }

    @Watch('percentControl')
    private onPercentControlChanged() {
        if (nodeDataSet) {
            console.log('Redrawing the network...');
            this.updateDataSets();
        }
    }

    private updateDataSets() {

        const compoundNames: string[] = this.compounds.map((compound: Compound) => {
            return compound.compoundName;
        });

        const matchingProfiles: ActivityProfile[] = this.activityProfiles
            .filter((profile: ActivityProfile) => {
                return profile.percentControl <= +this.percentControl;
            })
            .sort((a: ActivityProfile, b: ActivityProfile) => {
                if (b.percentControl < a.percentControl) {
                    return -1;
                }
                return b.percentControl > a.percentControl ? 1 : 0;
            });

        // Start with nodes for each compound
        const items: CompoundOrActivityProfileNode[] = [];
        compoundNames.forEach((compoundName: string) => {
            const image: string = 'img/benzene.svg';
            items.push({
                data: { compound: compoundName },
                id: compoundName, label: compoundName,
                image: image, color: this.compoundColor
            });
        });

        // Add a node for each kinase
        const edges: CompoundNetworkEdge[] = [];
        matchingProfiles.forEach((ap: ActivityProfile) => {

            const discoverx: string = ap.kinase.discoverxGeneSymbol;

            if (!items.filter((item: CompoundOrActivityProfileNode) => { return item.id === discoverx; }).length) {
                const image: string = `img/target.svg`;
                items.push({
                    data: { activityProfile: ap },
                    id: discoverx, label: discoverx,
                    image: image, color: this.kinaseColor
                });
            }

            edges.push({ from: ap.compoundName, to: discoverx,
                value: ap.percentControl,
                activityProfile: ap,
                title: `<table><tr><td>% Control:</td><td>${ap.percentControl}</td></tr>` +
                    `<tr><td>Kd:</td><td>${ap.kd}</td></tr></table>` });
        });

        // A DataView filters based on percent control and/or Kd
        if (!nodeDataSet || !edgeDataSet) { // Check for both to avoid tslint errors in else block
            nodeDataSet = new DataSet<CompoundOrActivityProfileNode>(items, { queue: true });
            edgeDataSet = new DataSet<Edge>(edges);
        }
        else {

            const existingEdges: string[] = edgeDataSet.getIds() as string[];
            const allNewEdges: string[] = edges.map((edge: CompoundNetworkEdge) => {
                return edge.id as string;
            });

            const edgesToRemove: string[] = existingEdges.filter((id: string) => {
                return allNewEdges.indexOf(id) === -1;
            });

            const edgesToAdd: string[] = allNewEdges.filter((id: string) => {
                return existingEdges.indexOf(id) === -1;
            });

            edgeDataSet.add(edges.filter((item: Edge) => {
                return edgesToAdd.indexOf(item.id as string) > -1;
            }));

            edgeDataSet.remove(edgesToRemove);

            const existingNodes: string[] = nodeDataSet.getIds() as string[];
            const allNewNodes: string[] = items.map((node: Node) => {
                return node.id as string;
            });

            const nodesToRemove: string[] = existingNodes.filter((id: string) => {
                return allNewNodes.indexOf(id) === -1;
            });

            const nodesToAdd: string[] = allNewNodes.filter((id: string) => {
                return existingNodes.indexOf(id) === -1;
            });

            nodeDataSet.add(items.filter((item: Node) => {
                return nodesToAdd.indexOf(item.id as string) > -1;
            }));

            nodeDataSet.remove(nodesToRemove);

            nodeDataSet.flush();
        }
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';
@height: 400px;

.compound-network-wrapper {
    min-height: @height;
    border: 1px solid lightgray;
    display: flex;
    align-content: center;
    justify-content: center;

    .compound-network-component {
        width: 100%;
        min-height: @height;
        height: @height; // Needed for visjs-generated div to fill parent

        div.vis-tooltip {
            font-family: 'Roboto' // Override visjs's default of 'verdana'
        }
    }
}
</style>
