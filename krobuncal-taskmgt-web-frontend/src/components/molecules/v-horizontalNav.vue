<template>
	<ul class="horizontal">
		<li v-for="link in links">
			<v-link :to="link.to" :name="link.name">
				{{ link.name }}
			</v-link>
		</li>
	</ul>
</template>

<script setup lang="ts">
import VLink from "../atoms/v-link.vue";

const links: { to: string; name: string }[] = [
	{
		to: "/",
		name: "home",
	},
	{
		to: "/about",
		name: "about",
	},
];
</script>

<style scoped>
.horizontal {
	display: flex;
	flex-direction: row;
	gap: 1rem;
}
li {
	list-style: none;
}
</style>
