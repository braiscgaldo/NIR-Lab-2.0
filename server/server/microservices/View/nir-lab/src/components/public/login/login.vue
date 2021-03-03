<template>
  <div class="login-page">
    <div>
      <Menu page='/login'/>
    </div>

    <div class="border_rect">
      <p class='title_info'>Welcome to our <strong>NIR-Lab</strong> Server!</p>
      <p class='info_text'>
        <ul>
          <li>
            In this server you will be able to process the data collected by the <strong>NIR-Lab mobile application</strong> in a simple, 
            easy, elegant and intuitive way.
          </li>
          <li>
            You will be able to generate new data sets from the native data collected by the application.
          </li>
          <li>
            You will also be able to perform training using neural networks with TensorFlow in a simple and easily modifiable way.
          </li>
          <li>
            Once the models have been trained you will be able to see the statistics related to them as well as make predictions with them.
          </li>
          <li>
            And last but not least, you have total control over your research! You can download both models and databases and delete them from the server as you wish!
          </li>
        </ul>
      </p>
        <p class='spam_info'>Don't wait any longer and log in, it's <strong>free</strong>!</p>
    </div>

    <div>
      <form class="vue-form" @submit.prevent="login">
      <fieldset>
        <legend>Login</legend>
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

.info_text {
  font-size: 1.2vw;
  text-align: justify;
  text-justify: inter-word;
}

.border_rect {
  width: auto;
  padding: 1vw;
}

.title_info {
  font-size: 1.5vw;
  text-align: center;
}

.spam_info {
  font-size: 1.3vw;
  text-align: center;
}

</style>
