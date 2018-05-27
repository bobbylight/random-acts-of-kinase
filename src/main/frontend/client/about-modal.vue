<template>
    <v-dialog v-model="visible" max-width="500px">
        <v-card>

            <v-card-title class="headline">Random Acts of Kinase</v-card-title>

            <v-card-text>

                <div class="build-info">
                    Version: [AIV]{version}[/AIV]<span class="about-metadata-splitter">|</span>[AIV]{date}[/AIV]
                </div>

                <div>
                    <div class="about-image">
                        <img src="img/molecule.svg"><!-- :src="aboutImagePath">-->
                    </div>
                    <div class="description">
                        <div class="ui header">A kinase search tool.</div>
                        <p>There's not much going on here just yet.</p>
                    </div>
                </div>

                <div class="thanks">
                    <div class="subheading">Special thanks:</div>
                    <ul>
                        <li>
                            <div>"molecule" icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a>
                                from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
                        </li>
                        <li>
                            <div>Splash screen photo by Drew Hays on Unsplash</div>
                        </li>
                    </ul>
                </div>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="success" @click="visible = false">Close</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component
export default class AboutModal extends Vue {

    @Prop({ required: true })
    show: boolean;

    get visible() {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.$emit('close');
        }
    }
}
</script>

<style lang="less">
.about-image {
    float: left;

    img {
        width: 48px;
        height: 48px;
        margin-right: 2rem;
    }
}

.thanks {

    padding-top: 1rem;

    ul {
        padding: 0 2rem;
    }
}

.build-info {

    position: absolute;
    bottom: 0.5rem;
    left: 0.5rem;
    color: gray;

    .about-metadata-splitter {
        margin: 0 .5rem;
    }
}
</style>
