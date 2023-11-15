<template>
    <VInput type="text" placeholder="Enter first name" v-model="username"/>
    <VInput type="text" placeholder="Enter first name" v-model="password"/>
    <VButton type="submit" @click="login()">Login</VButton>
</template>

<script setup lang="ts">
import {ref} from "vue";
import VInput from "../atoms/v-input.vue"
import VButton from "../atoms/v-button.vue";
import router from "@/router";

const username = ref('')
const password = ref('')

const login = () => {

    const formData = new FormData()
    formData.append("username", username.value)
    formData.append("password", password.value)
    fetch("http://localhost:9000/login", {
        method: "POST",
        body: formData,
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
            
            localStorage.setItem("id", data.id)
            router.push("/")
        })

}
</script>