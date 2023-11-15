<template>
	<HomePage class="main-container">
		<template #main>
			<div class="main">
				<div class="left">
					<h2>List of Boards</h2>
					<VBoard :items="board.boards"/>
					<VButton @click="modal.open = true">Add Boards</VButton>
				</div>
				<div class="right">
					<VAddBoard v-if="modal.open"/>
					<VAddMember v-if="modal.openMember"/>
					<VAddTask v-if="modal.openTask"/>
					<VAddComment v-if="modal.openComment"/>
					<VEditBoard v-if="modal.openEditBoard"/>
					<VEditTask v-if="modal.openEditTask"/>
					<VTask/>
				</div>
			</div>
		</template>
	</HomePage>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import HomePage from "../template/HomePage.vue";
import { useModal } from "../../stores/page"
import VLogo from "../atoms/v-logo.vue";
import VSpan from "../atoms/v-span.vue";
import VVerticalNav from "../molecules/v-verticalNav.vue";
import VAddBoard from "../organisms/v-addBoard.vue";
import VAddMember from "../organisms/v-addMember.vue";
import VAddComment from "../organisms/v-addComment.vue";
import VEditTask from "../organisms/v-editTask.vue";
import VEditBoard from "../organisms/v-editBoard.vue";
import VAddTask from "../organisms/v-addTask.vue";
import VButton from "../atoms/v-button.vue"
import { usePage } from "../../stores/page";
import VBoard from "../organisms/v-boards.vue"
import VTask from "../organisms/v-tasks.vue"
import { useBoard } from "../../stores/page"

const page = usePage();
const modal = useModal()
const board = useBoard()
const spanValue = ref("Platform Launch");
const withImg = true;
const source = "../../public/task.png";
const links: { to: string; name: string }[] = [
	{
		to: "/",
		name: "Platform Launch",
	},
	{
		to: "/",
		name: "Marketing Plan",
	},
	{
		to: "/",
		name: "Roadmap",
	},
];

const openModal = () => {
	modal.open = true;
	console.log(modal.open);
};

const socket = new WebSocket("ws://localhost:9000/socket");

socket.onopen = () => {
	console.log("connected");
}

socket.onmessage = (event) => {
	const data = JSON.parse((event.data))
	const id = localStorage.getItem("id")
	board.boards = data.board
	board.boardMembers = data.boardMember
	// const a = board.boardMembers.filter(bm => bm.memberId===id)
	// console.log(a)
	board.tasks = data.task
	board.comments = data.comment
	//boardMember: Array [ {…} ]
 //0: Object { boardId: "e24ea9ea-a6cc-4af9-92d5-ca0debb75bcd", memberId: "7bfc2250-c17c-437a-8d4a-359a22d01736" }


}

socket.onclose = () => {
	console.log("disconnected");
}
onMounted(() => {
    fetch("http://localhost:9000/board", {
        credentials: 'include'
    })
        .then((response) => {
            if(response.ok) {
                return response.json()
            }else{
                throw new Error("network error response")
            }
        })
            .then((data) => {
				board.boards = [...data,...board.boards]
                console.log(data)
            })

	fetch("http://localhost:9000/users", {
        credentials: 'include'
    })
        .then((response) => {
            if(response.ok) {
                return response.json()
            }else{
                throw new Error("network error response")
            }
        })
            .then((data) => {
				board.users = data
				board.user.name = board.users[0].name
                console.log(data)
            })
})

const listTag = "ul";
const listItemTag = "li";
</script>

<style scoped>
.addButton {
	background-color: var(--violet);
	padding: 0.5rem;
	border-radius: 20px;
	cursor: default;
}

.header-title {
	font-weight: bolder;
}
</style>
