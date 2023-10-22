<template>
  <div class="item">
    <i>
      <a href="#">
        <slot name="icon"></slot>
      </a>
    </i>
    <div class="details">
      <h3>
        <slot name="heading"></slot>
      </h3>
    </div>
  </div>
</template>

<style scoped>
.item {
  margin-top: 2rem;
  display: flex;
}

.details {
  display: flex;
  place-items: center;
  place-content: center;
  width: 200px;
  border: 1px solid var(--color-border);
  background: var(--color-background);
  border-radius: 8px;
  margin-left: 1rem;
}

i {
  display: flex;
  place-items: center;
  place-content: center;
  width: 200px;
  border: 1px solid var(--color-border);
  background: var(--color-background);
  border-radius: 8px;

  color: var(--color-text);
}

h3 {
  font-size: 1.2rem;
  font-weight: 500;
  color: var(--color-heading);
}
</style>
