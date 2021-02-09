<template>

<div>

  <b-navbar toggleable="lg" type="dark" variant="info" class="menu">
    <b-navbar-brand href="/">NIR-LAB</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav v-for="(nav_item, idx) in nav_items" :key="idx">
        <router-link :to="{path:nav_item.router}" :style="{color:nav_item.color}">{{  nav_item.name  }}</router-link>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
  <router-view/>
</div>
</template>

<script>
export default {
    data(){
        return {
            nav_items: [
                {
                  name: 'Data Treatment',
                  router: '/data_treatment',
                  color: this.page == '/data_treatment' ? '#FFF' : '#DDD'  
                }, 
                {
                  name: 'Training',
                  router: '/training',
                  color: this.page == '/training' ? '#FFF' : '#DDD'  
                }, 
                {
                  name: 'Model Logs',
                  router: '/model_logs',
                  color: this.page == '/model_logs' ? '#FFF' : '#DDD'  
                },
                {
                  name: 'Git Hub',
                  router: '/gitgub',
                  color: this.page == '/gitgub' ? '#FFF' : '#DDD'  
                }, 
                {
                  name: 'Mobile App',
                  router: '/mobile_app',
                  color: this.page == '/mobile_app' ? '#FFF' : '#DDD'
                }, 
                {
                  name: 'Information',
                  router: '/information',
                  color: this.page == '/information' ? '#FFF' : '#DDD'
                },
                {
                  name: 'Contributers',
                  router: '/contributers',
                  color: this.page == '/contributers' ? '#FFF' : '#DDD'
                },
                {
                  name: 'Acknowledgements',
                  router: '/acknowledgements',
                  color: this.page == '/acknowledgements' ? '#FFF' : '#DDD'
                },
                {
                  name: 'Edit Profile',
                  router: '/edit_profile',
                  color: this.page == '/edit_profile' ? '#FFF' : '#DDD'
                }
                ],
        }
    },
    props: {
      page: String,
      name: String
    }
}
</script>

<style scoped>
.bg-info {
    background-color: #EE3744 !important;
}
</style>