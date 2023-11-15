<template>
	<a> <slot /> </a>
</template>

<script setup lang="ts">
defineProps(["to", "name"]);
</script>

<style scoped>
a {
	cursor: default;
}
</style>
