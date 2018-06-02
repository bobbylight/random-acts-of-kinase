<template>
   <v-btn flat class="activity-or-kd-toggle" @click="toggle">
       {{suffix}}
       <v-icon right>arrow_drop_down</v-icon>
   </v-btn>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component({ components: { } })
export default class ActivityOrKdToggleButton extends Vue {

    private suffix: string = '%';

    private toggle() {
        this.suffix = this.suffix === '%' ? 'nM' : '%';
        this.$store.commit('setFilterType', this.suffix === '%' ? 'percentControl' : 'kd');
    }
}
</script>

<style lang="less">
@import '../../styles/app-variables';

.activity-or-kd-toggle {

    margin: 0;
    height: initial;
    min-width: initial;
    padding: 0 6px;

    .btn__content {

        padding: 0;
        font-weight: initial; // Default Material theme makes button text a little bold
        text-transform: none; // Don't capitalize "nM"
        color: rgba(0, 0, 0, 0.54);

        &:hover {
            color: @primary;

            .icon.material-icons {
                color: @primary;
            }
        }

        .icon.material-icons {
            margin-left: 0;
        }
    }
}
</style>
