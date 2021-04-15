<template>

<div>

  <b-navbar toggleable="lg" type="dark" variant="info" class="menu">
    <b-navbar-brand href="/data_treatment">NIR-LAB</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav v-for="(nav_item, idx) in nav_items" :key="idx">
        <router-link :to="{path:nav_item.router}" :style="{color:nav_item.color}">{{  nav_item.name  }}</router-link>
      </b-navbar-nav>
    </b-collapse>
    <b-navbar-brand href="/" @click="logout">LOG OUT</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
  </b-navbar>
  <router-view/>
</div>
</template>

<script>
const axios = require('axios');
axios.defaults.headers.post['Content-Type'] = 'application/json';

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
                  name: 'Logs',
                  router: '/logs',
                  color: this.page == '/logs' ? '#FFF' : '#DDD'  
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
      page: String
    },
    methods: {
      logout() {
        var data = {
          type: "Logout"
        };
        this.$store.commit('logout', {});
        console.log(this.$store)
        axios.delete('http://localhost:4000/', data).then(response => {
          if (response.status == 200){
            console.log('logged out');
            // route to data treatment
            this.$router.push({name: '/',  query: { redirect: '/' } });
          }
        })
      }
    }
}
</script>

<style scoped>
.bg-info {
    background-color: #EE3744 !important;
}
</style>