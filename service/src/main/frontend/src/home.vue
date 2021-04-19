<template>
    <div class="home-wrapper">

        <div class="parallax hidden">
            <h1 class="display-3 words-in-image">We Are Open Science</h1>
        </div>

        <v-row wrap class="home-text-section justify-center">
            <v-col cols="8" lg="6">
                <h1>Advancing Drug Discovery Through Open Collaboration</h1>
                <div class="pt-3">
                    The SGC-UNC team of scientists is dedicated to discovering and openly sharing selective, small molecule
                    inhibitors of protein kinases. Their work aims to expand drug discovery across this important class of
                    disease-relevant proteins and help speed the creation of new medicines for patients.
                </div>
                <div class="pt-3">
                    The human genome contains hundreds of dark kinases that are poorly characterized and for which
                    little is known about their role in human disease. SGC-UNC scientists have championed a method of
                    studying the biology of dark kinases, called chemogenomics. The approach utilizes ATP-competitive
                    kinase inhibitors that show cross-activity on multiple kinases. The most useful 'narrow-spectrum'
                    inhibitors have activity on 2 to 10 kinases. SGC-UNC scientists have assembled a large matrix of
                    these narrow-spectrum kinase inhibitors into a chemogenomic set to probe the cell biology of the
                    dark kinases.
                </div>
                <div class="pt-3">
                    KCGS has been designed to replace PKIS and PKIS2, supplies of which have been depleted. KCGS has
                    improved coverage of the human kinome and contains no promiscuous compounds, thereby facilitating
                    the annotation of phenotypic data and hypothesis generation. KCGS is now available from the SGC and
                    you can find the activity data we collected on the inhibitors using the search tab.
                </div>
            </v-col>
        </v-row>

        <v-row wrap class="home-text-section justify-center grey lighten-3">
            <v-col cols="8" lg="6">
                <h1>About This Tool</h1>
                <div class="pt-3">
                    Random Acts of Kinase allows you to search for compound activity data collected on compounds in
                    KCGS. If you have any feedback or questions, please contact
                    <a href="mailto:SGC-UNC@unc.edu">SGC-UNC@unc.edu</a>. To request a physical copy of the set please
                    go to <a href="https://ximbio.com/reagent/157681/the-kinase-chemogenomic-set-kcgs/">Ximbio</a>.
                </div>
            </v-col>
        </v-row>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class Home extends Vue {

    created() {
        setTimeout(() => {
            document.getElementsByClassName('parallax')[0].classList.remove('hidden');
        }, 100);
    }
}
</script>

<style lang="less">
.home-wrapper {

    height: 100%;

    .home-text-section {
        padding: 4rem 0;
    }

    .parallax {
        transition: opacity 1s;
        background-image: url(/img/drew-hays-206414-unsplash-small.jpg);
        min-height: 600px;

        background-position: center;
        background-repeat: no-repeat;
        background-size: cover;

        .words-in-image {
            color: white;
            padding: 3rem 0 0 3rem;
        }

        &.hidden {
            opacity: 0;
        }
    }
}
</style>
