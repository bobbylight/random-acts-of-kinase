<template>
    <div class="compound-network-wrapper">
        <loading-mask v-if="loading"></loading-mask>
        <div v-show="!loading" ref="networkDiv"></div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import { Color, Data, DataSet, Edge, Network, Node, Options } from 'vis';
import restApi from './rest-api';
import { ActivityProfile, Compound, ErrorResponse } from './rak';
import Toaster from './toaster';
import LoadingMask from './loading-mask.vue';

@Component({ components: { LoadingMask } })
export default class CompoundNetwork extends Vue {

    @Prop({ required: true })
    private compounds: Compound[];

    @Prop({ required: true })
    private filters: any;

    private loading: boolean = false;

    private compoundColor: Color = {
        border: '#b0b09f',
        background: '#d0d0ff'
    };

    private kinaseColor: Color = {
        border: '#9fb0b0',
        background: '#ffd0d0'
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

        setTimeout(() => {
            restApi.getAllActivityProfiles(compoundNames, this.filters)
                .then((activityProfiles: ActivityProfile[]) => {

                    const matchingProfiles: ActivityProfile[] = activityProfiles
                        .sort((a: ActivityProfile, b: ActivityProfile) => {
                            if (b.percentControl < a.percentControl) {
                                return -1;
                            }
                            return b.percentControl > a.percentControl ? 1 : 0;
                        });

                    //// Keep network more responsive by showing only top 100 matches for now
                    //if (matchingProfiles.length > 51) {
                    //    matchingProfiles.length = 51;
                    //}

                    const nameToNodeMap: any = {};

                    // Start with nodes for each compound
                    const items: Node[] = [];
                    compoundNames.forEach((compoundName: string) => {
                        const image: string = `api/compounds/images/${compoundName}?width=44&height=44`;
                        items.push({
                            id: items.length + 1, label: compoundName,
                            image: image, color: this.compoundColor
                        });
                        nameToNodeMap[ compoundName ] = items.length;
                    });

                    // Add a node for each kinase
                    const edges: Edge[] = [];
                    matchingProfiles.forEach((ap: ActivityProfile) => {

                        const discoverx: string = ap.kinase.discoverxGeneSymbol;
                        let kinaseNodeIndex: number = nameToNodeMap[ discoverx ] || -1;

                        if (kinaseNodeIndex === -1) {
                            const image: string = `img/molecule.svg`;
                            items.push({
                                id: items.length + 1, label: discoverx,
                                image: image, color: this.kinaseColor
                            });
                            kinaseNodeIndex = nameToNodeMap[ discoverx ] = items.length;
                        }

                        edges.push({ from: nameToNodeMap[ ap.compoundName ], to: kinaseNodeIndex });
                    });

                    const nodes: DataSet<Node> = new DataSet<Node>(items);
                    const edgeData: DataSet<Edge> = new DataSet<Edge>(edges);

                    // create a network
                    const container: HTMLElement = this.$refs.networkDiv as HTMLElement;

                    // provide the data in the vis format
                    const data: Data = {
                        nodes: nodes,
                        edges: edgeData
                    };
                    const options: Options = {
                        nodes: {
                            shape: 'circularImage',
                            size: 48,
                            borderWidth: 3
                        },
                        edges: {
                            color: '#d0d0d0',
                            value: 8
                        },
                        layout: {
                            improvedLayout: false
                        },
                        physics: {
                            repulsion: {
                                nodeDistance: 200
                            }
                        }
                    };

                    this.loading = false;

                    setTimeout(() => {
                        const network: Network = new Network(container, data, options);

                        network.on('oncontext', (params: any) => {
                            params.event.preventDefault();
                            console.log(`Selected nodes: ${network.getSelectedNodes()}, ` +
                                `selected edges: ${network.getSelectedEdges()}`);
                        });
                    }, 0);
                })
                .catch((e: ErrorResponse) => {
                    this.loading = false;
                    Toaster.error(e.message);
                });
        }, 3000);
    }

    @Watch('compounds')
    onCompoundUpdated() {
        this.redraw();
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';

.compound-network {
    width: 100%;
    height: 400px;
    border: 1px solid lightgray;
}
</style>
