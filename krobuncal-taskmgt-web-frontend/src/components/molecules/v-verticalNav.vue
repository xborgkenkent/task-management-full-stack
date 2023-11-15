<template>
	<ul class="vertical">
		<li v-for="link in links" class="items">
			<v-link
				:to="link.to"
				:name="link.name"
				class="items"
				@click="setPage(link.name)"
			>
				<img v-show="withImg" :src="source" />
				<p>{{ link.name }}</p>
			</v-link>
		</li>
	</ul>
</template>

<script setup lang="ts">
import VLink from "../atoms/v-link.vue";
import { usePage } from "../../stores/page";
defineProps(["links", "withImg", "source"]);

const page = usePage();
const setPage = (name: string) => {
	page.page = name;
	console.log(page.page);
};
</script>

<style scoped>
.vertical {
	display: flex;
	flex-direction: column;
	gap: 1rem;
}
.items {
	display: flex;
	color: var(--white);
	text-decoration: none;
	padding-left: 0.5rem;
	padding-top: 0.2rem;
	padding-bottom: 0.2rem;
	width: 18vw;
	border-top-right-radius: 15px;
	border-bottom-right-radius: 15px;
}
li:hover {
	background-color: var(--violet);
}
</style>
