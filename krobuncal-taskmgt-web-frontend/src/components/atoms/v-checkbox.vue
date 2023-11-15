<template>
	<input type="checkbox" :value="labelValue" />
	<label :for="name"> {{ labelValue }}</label>
</template>

<script setup lang="ts">
defineProps(["name", "labelValue"]);
</script>

<style scoped></style>
