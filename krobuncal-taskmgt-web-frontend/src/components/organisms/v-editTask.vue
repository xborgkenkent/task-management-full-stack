<template>
	<Teleport to="body">
		<div v-if="(modal.openEditTask = true)" class="modal">
			<div class="addForm">
				<v-input
					class="input"
					:inputType="inputType"
					placeholder="Enter task name"
					v-model="board.selectedTask.caption"
				/>
				<v-button class="submitButton" @click="editTask()">Edit Task</v-button>
			</div>
			<div class="close">
				<v-button class="closeButton" @click="modal.openEditTask = false">x</v-button>
			</div>
		</div>
	</Teleport>
</template>

<script setup lang="ts">
import { watch, ref } from "vue";
import { usePage } from "../../stores/page";
import VInput from "../atoms/v-input.vue";
import VSelect from "../atoms/v-select.vue";
import { useModal } from "../../stores/page";
import { useBoard } from "../../stores/page";

const board = useBoard();
const page = usePage();
const modal = useModal()
const inputType = "text";

const taskname = ref('')

const editTask = () => {

	console.log(board.user, board.boardId)
	const formData = new FormData()
	formData.append("caption", board.selectedTask.caption)
	fetch(`http://localhost:9000/board/task/edit/${board.selectedTask.id}`, {
		method: "POST",
		body: formData,
		credentials: 'include',
	})
		.then((response) => {
			if(response.ok) {
				return response.ok
			}else{
				throw new Error("network error response")
			}
		})
}
</script>

<style scoped>
.modal {
	display: flex;
	justify-content: space-between;
	position: fixed;
	z-index: 999;
	top: 40%;
	left: 40%;
	width: 30vw;
	background-color: var(--jaguar);
	padding: 3rem;
	border-radius: 10px;
}

.addForm {
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	gap: 1rem;
}
.input {
	padding: 0.5rem;
	width: 25vw;
}

.select {
	padding: 0.5rem;
	width: 26vw;
}
.submitButton {
	background-color: var(--violet);
	width: 25vw;
	padding: 0.5rem;
	text-align: center;
	border-radius: 10px;
}
.closeButton {
	background-color: var(--red);
	font-weight: bolder;
	padding: 0.2rem;
	border-radius: 10px;
	width: 5vw;
	cursor: default;
}
</style>
