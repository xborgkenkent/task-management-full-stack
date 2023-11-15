<template>
    <VInput type="text" placeholder="Enter first name" v-model="firstname"/>
    <VInput type="text" placeholder="Enter first name" v-model="middlename"/>
    <VInput type="text" placeholder="Enter first name" v-model="lastname"/>
    <VInput type="text" placeholder="Enter first name" v-model="username"/>
    <VInput type="text" placeholder="Enter first name" v-model="password"/>
    <VButton type="submit" @click="register()">Register</VButton>
</template>

<script setup lang="ts">
import {ref} from "vue";
import VInput from "../atoms/v-input.vue"
import VButton from "../atoms/v-button.vue";
import router from "@/router";

const firstname = ref('')
const middlename = ref('')
const lastname = ref('')
const username = ref('')
const password = ref('')

const register = () => {

    const formData = new FormData()
    formData.append("firstname", firstname.value)
    formData.append("middlename", middlename.value)
    formData.append("lastname", lastname.value)
    formData.append("username", username.value)
    formData.append("password", password.value)
    fetch("http://localhost:9000/register", {
        method: "POST",
        body: formData
    })
    .then((response) => {
        if(response.ok){
            router.push("/login")
        }else{
            throw new Error("network response error")
        }
    })
}
</script>