<template>
    <VInput type="text" placeholder="Enter first name" v-model="fname"/>
</template>

<script setup lang="ts">
import {ref} from "vue";
import VInput from "../atoms/v-input.vue"


const fname = ref('firsntma')
</script>