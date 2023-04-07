<script setup lang="ts">
defineProps<{
  msg: string
}>()
</script>

<template>
  <div class="greetings">
    <h1 class="green">{{ msg }}</h1>
    <h3>
      You’ve successfully created an application with VueJS and Spring Boot
    </h3>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  top: -10px;
  border: 1px solid var(--color-border);
  background: var(--color-background);
  border-radius: 8px;
  text-align: center;
}

h3 {
  font-size: 1.2rem;
  text-align: left;
}

</style>
