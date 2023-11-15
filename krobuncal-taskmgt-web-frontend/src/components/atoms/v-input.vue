<template>
	<input :type="inputType" :placeholder="placeholderValue" :value="modelValue" @input="$emit('update:modelValue', $event.target.value)"/>
</template>

<script setup lang="ts">
	defineProps(['modelValue', 'placeholderValue', 'inputType'])
	defineEmits(['update:modelValue'])
</script>

<style scoped>
	
</style>