<template>
	<select
		:value="modelValue"
		@change="$emit('update:modelValue', $event.target.value)"
	>
		<option v-for="item in items" :key="item.id" :value="item.id">
			{{ item.name }}
		</option>
	</select>
</template>

<script setup lang="ts">
defineProps(["modelValue", "items"]);
defineEmits(["update:modelValue"]);
</script>

<style scoped></style>
