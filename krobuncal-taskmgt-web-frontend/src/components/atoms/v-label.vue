<template>
	<label :for="name">{{ labelName }}</label>
</template>

<script setup lang="ts">
defineProps(["labelName", "name"]);
</script>
