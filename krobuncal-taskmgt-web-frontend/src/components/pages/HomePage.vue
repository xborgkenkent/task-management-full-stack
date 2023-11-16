<template>
	<HomePage class="main-container">
		<template #main>
			<div class="main">
				<div class="left">
					<h2>List of Boards</h2>
					<VBoard :items="board.boards" v-if="board.boards.length>0"/>
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
	console.debug(modal.open);
};

const socket = new WebSocket("ws://localhost:9000/socket");

socket.onopen = () => {
	console.debug("connected");
}

socket.onmessage = (event) => {
	const data = JSON.parse((event.data))
	const id = localStorage.getItem("id")
	board.boards = data.boardMember[1]
	board.boardMembers = data.boardMember[0]

	console.log(board.boards)
	console.log(board.boardMembers)



	board.tasks = data.task
	board.comments = data.comment
}

socket.onclose = () => {
	console.debug("disconnected");
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
				data.map((item) => {
					board.boards = item[1]
					board.boardMembers = item[0]
				})

				console.log(board.boards)
				// board.boards = data[1]
				// board.boardMembers = data[0]
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
                console.debug(data)
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
