<template>
	<button :type="buttonType">
		<slot />
	</button>
</template>

<script setup lang="ts">
defineProps(["buttonType"]);
</script>

<style scoped></style>
