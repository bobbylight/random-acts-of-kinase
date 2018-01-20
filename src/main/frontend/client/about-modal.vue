<template>
    <div class="ui modal">
        <i class="close icon"></i>
        <div class="header">
            Random Acts of Kinase
        </div>
        <div class="build-info">
            Build: {{version}}
        </div>
        <div class="image content">
            <div class="ui small image">
                <img src="img/molecule.svg"><!-- :src="aboutImagePath">-->
            </div>
            <div class="description">
                <div class="ui header">A kinase search tool.</div>
                <p>There's not much going on here just yet.</p>
                <p>
                    Special thanks:
                    <ul>
                        <li>
                            <div>"molecule" icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
                        </li>
                    </ul>
                </p>
            </div>
        </div>
        <div class="actions">
            <div class="ui positive right button">
                Close
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class AboutModal extends Vue {

    version: string;

    created() {
        this.version = `0.3.0 (${new Date().toLocaleDateString()})`;
    }
}
</script>

<style lang="less">
    .build-info {
        position: absolute;
        bottom: 0.5rem;
        left: 0.5rem;
        color: gray;
    }
</style>
