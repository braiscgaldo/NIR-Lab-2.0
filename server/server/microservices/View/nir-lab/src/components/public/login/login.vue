<template>
  <div class="login-page">
    <div>
      <Menu page='/login'/>
    </div>
    <div>
      <form class="vue-form" @submit.prevent="login">


    <fieldset>
      <legend>Login into system!!</legend>
      <div>
        <label class="label" for="email">Email</label>
        <input type="text" name="email" id="email" required v-model="email">
      </div>
      <div>
        <label class="label" for="password">Password</label>
        <input type="password" name="password" id="password" required v-model="password">
      </div>
      <p class="little">If you don't have an account you can create one <router-link to="/create_account">clicking here</router-link></p>
      <div>
        <input type="submit" value="Login">
      </div>
    </fieldset>
  </form>


  </div>
 
    <div>
      <Footer/>
    </div>
  </div>
</template>

<script>
import Menu from "../../common/header/public/menu.vue"
import Footer from "../../common/footer/footer.vue";

export default {
  data: () => ({
    email: "",
    password: ""
  }),
  components: {
    Menu, 
    Footer
  },
  methods: {
    login() {
      console.log(this.email);
      console.log(this.password);
      // route to data treatment
      this.$router.push({name: 'data_treatment',  query: { redirect: '/data_treatment' } });
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.login-page{
  background-color: #DEEAEE;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

h1 {
  margin-top: 4vw;
  text-align: center;
}

.little{
  font-size: 0.9vw;
  text-align: center;
}

</style>
