<template>

<div>

  <b-navbar toggleable="lg" type="dark" variant="info">
    <b-navbar-brand href="#">NIR-LAB</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav v-for="(nav_item, idx) in nav_items" :key="idx">
        <b-nav-item :href="nav_item.url">{{  nav_item.name  }}</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</div>
</template>

<script>
export default {
    data(){
        return {
            nav_items: [
                {
                  name: 'Data Treatment',
                  link: '#',
                  activated: false
                }, 
                {
                  name: 'Training',
                  link: '#',
                  activated: false
                },
                {
                  name: 'Git Hub',
                  link: '#',
                  activated: false  
                }, 
                {
                  name: 'Mobile App',
                  link: '#',
                  activated: false
                }, 
                {
                  name: 'Information',
                  link: '#',
                  activated: false
                },
                {
                  name: 'Contributers',
                  link: '#',
                  activated: false
                },
                {
                  name: 'Agreements',
                  link: '#',
                  activated: false
                }
                ]
        }
    }
}
</script>

<style scoped>
.bg-info {
    background-color: #EE3744 !important;
}
</style>