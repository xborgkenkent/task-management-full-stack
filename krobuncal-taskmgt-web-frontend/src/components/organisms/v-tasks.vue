<template>
    <div class="title-header">
        <h2>{{ board.title }}</h2>
        <VButton @click="modal.openMember = true">add member</VButton>
        <VButton @click="modal.openTask = true">add task</VButton>
    </div>
    <div class="tasks">
        <VList tag="ul" v-for="item in board.tasks.filter(b=>b.boardId===board.boardId)">
            <VListItem tag="li" class="task">
                <VButton class="comment-btn" @click="openModal(item.id)">Comment</VButton>
                <VButton class="comment-btn" @click="openEditTask(item)">Edit</VButton>
                <h3>Title</h3>
                {{ item.caption }}
                <h3>Assigned ID</h3>
                {{ item.assign}}
            </VListItem>
            <h2>Comments</h2>
            <div class="comments">
                <VList tag="ul" v-for="item in board.comments.filter(c=>c.taskId===item.id)">
                    <VListItem tag="li" class="comment">
                        {{item.comment}}
                    </VListItem>
                </VList>
            </div>
        </VList>
    </div>
</template>

<script setup lang="ts">
import {ref} from "vue"
import VList from "../atoms/v-list.vue"
import VListItem from "../atoms/v-list-item.vue"
import VInput from "../atoms/v-input.vue"
import VButton from "../atoms/v-button.vue"
import { useBoard } from "../../stores/page"
import { useModal } from "../../stores/page"
const board = useBoard()
const modal = useModal()

const comment = ref('')

const openModal = (id: string) => {
    modal.openComment = true
    board.taskId = id
    console.log(id)
}

const openEditTask = (item) => {
    modal.openEditTask = true
    board.selectedTask.id = item.id
    board.selectedTask.caption = item.caption
    console.log(item)
}
</script>

<style scoped>


.task {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    border: 1px solid white;
}

.task {
    display: flex;
    flex-direction: column;
    gap: 1rem
}

.comments {
    display: flex;
    flex-direction: column;
    gap: .5rem;
    border: 1px solid white;
}
</style>