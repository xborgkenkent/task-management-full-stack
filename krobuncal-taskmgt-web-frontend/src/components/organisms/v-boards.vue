<template>
    <VList tag="ul" v-for="item in items">
            <VListItem tag="li">
            <div class="task">
                <div @click="board.fetchTask(item.id, item.name)">
                    {{ item.name }}
                </div>
                <VButton @click="openModal(item)">update board</VButton>
                <VButton @click="board.deleteBoard(item.id)">delete board</VButton>
                
            </div>
            </VListItem>
        
    </VList>
</template>

<script setup lang="ts">
import { ref, onMounted} from "vue"
import VList from "../atoms/v-list.vue"
import VButton from "../atoms/v-button.vue"
import VListItem from "../atoms/v-list-item.vue"
import { useBoard } from "../../stores/page"
import { useModal } from "../../stores/page"
const board = useBoard()
const modal = useModal()

const openModal = (item) => {
    const data = JSON.parse(JSON.stringify(item))
    console.debug(data)
    board.selectedBoard.id = data.id
    console.debug(board.selectedBoard.id)
    board.selectedBoard.name = data.name
    board.selectedBoard.ownerId = data.ownerId
    modal.openEditBoard = true
}
defineProps(["items"])
</script>

<style scoped>
 .task {
    display: flex;
    gap: 1rem;
    justify-content: space-evenly;
 }
</style>