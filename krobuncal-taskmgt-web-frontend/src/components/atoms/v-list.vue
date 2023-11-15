<template>
	<component :is="tag">
		<slot />
	</component>
</template>

<script setup lang="ts">
defineProps(["tag"]);
</script>

<style scoped></style>
