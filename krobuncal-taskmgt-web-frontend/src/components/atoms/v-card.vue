<template>
	<div :class="className">
		<slot />
	</div>
</template>

<script setup lang="ts">
defineProps(["className"]);
</script>

<style></style>
