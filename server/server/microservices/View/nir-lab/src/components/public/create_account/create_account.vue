<template>
  <div class="createAcc-page">
    <div>
      <Menu page='/create_account'/>
    </div>
    <div>
      <form class="vue-form" @submit.prevent="create_account">


    <fieldset>
      <legend>Create your account!!</legend>
      <div>
        <label class="label" for="username">Username</label>
        <input type="text" name="username" id="password" required v-model="username">
      </div>
      <div>
        <label class="label" for="password">Password</label>
        <input type="password" name="password" id="password" required v-model="password">
      </div>
      <div>
        <label class="label" for="repeated_password">Repeat Password</label>
        <input type="password" name="repeated_password" id="repeated_password" required v-model="repeated_password">
      </div>
      <div>
        <label class="label" for="name">Name</label>
        <input type="text" name="name" id="name" required v-model="name">
      </div>
      <div>
        <label class="label" for="surname">Surname</label>
        <input type="text" name="surname" id="surname" required v-model="surname">
      </div>
      <div>
        <label class="label" for="email">Email</label>
        <input type="text" name="email" id="email" required v-model="email">
      </div>
      <p class="little">If you already have an account you can login <router-link to="/login">clicking here</router-link></p>
      <div>
        <input type="submit" value="Create Account">
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
const axios = require('axios')


export default {
  data: () => ({
    username: "",
    password: "",
    repeated_password: "",
    name: "",
    surname: "",
    email: ""

  }),
  components: {
    Menu, 
    Footer
  },
  methods: {
    create_account() {
      var data = {
        type: "CreateUser",
        username_auth: this.username,
        password_auth: this.password,
        name_auth: this.name,
        surname_auth: this.surname,
        email_auth: this.email
      }
      axios.put('http://localhost:4000/create_user', data).then(response => {
        console.log('code: ' + response.status)
      })
      this.$router.push({name: 'data_treatment',  query: { redirect: '/data_treatment' } });
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.createAcc-page{
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
