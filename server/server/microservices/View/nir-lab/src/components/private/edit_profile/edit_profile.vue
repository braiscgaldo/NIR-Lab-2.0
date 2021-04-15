<template>
  <div v-if="this.$store['state']['user'] != ''" class="editAcc-page">
    <div>
      <Menu page='/edit_account' name="brais"/>
    </div>
    <div>
      <form class="vue-form" @submit.prevent="edit">

    <fieldset>
      <legend>Edit your account!!</legend>
      <div>
        <label class="label" for="email">Email</label>
        <input type="text" name="email" id="email" required v-model="email">
      </div>
      <div>
        <label class="label" for="password">Password</label>
        <input type="password" name="password" id="password" required v-model="password">
      </div>
      <div>
        <label class="label" for="new_password">Repeat Password</label>
        <input type="password" name="new_password" id="new_password" required v-model="new_password">
      </div>
      <div>
        <label class="label" for="name">Name</label>
        <input type="text" name="name" id="name" required v-model="name">
      </div>
      <div>
        <label class="label" for="surname">Surname</label>
        <input type="text" name="surname" id="surname" required v-model="surname">
        <input class="sb" type="submit" value="Edit Account">
      </div>
    </fieldset>
  </form>
  <VueModal v-model="showModalUpdatedUser" title="User updated">
    <p>
      The user has been updated!!!!
    </p>
  </VueModal>
  <VueModal v-model="showModalFailedUpdate" title="Error updating user">
    <p>
      Error updating user! Please, check your credentials!
    </p>
  </VueModal>


  </div>
 
    <div>
      <Footer/>
    </div>
  </div>
</template>

<script>
import Menu from "../../common/header/private/menu.vue"
import Footer from "../../common/footer/footer.vue";
const axioslib = require('axios');
const axios = axioslib.create({
  headers: {
    'Access-Control-Allow-Origin': '*'
  }
});
axios.defaults.headers.post['Content-Type'] = 'application/json';
// dialogs
import VueModal from '@kouts/vue-modal';

export default {
  data: () => ({
    password: "",
    new_password: "",
    name: "",
    surname: "",
    email: "", 
    showModalUpdatedUser: false,
    showModalFailedUpdate: false

  }),
  components: {
    Menu, 
    Footer,
    VueModal
  },
  methods: {
    edit() {
      var data = {
        username_auth: this.$store['state']['user'],
        password_auth: this.password,
        name_auth: this.name,
        surname_auth: this.surname,
        email_auth: this.email,
        new_password_auth: this.new_password
      }  
      axios.post('http://localhost:4000/edit_user', data).then(response => {
        if (response.status == 200){
          console.log('user updated');
          this.showModalUpdatedUser = true;
        }else{
          this.showModalFailedUpdate = true;
        }
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.editAcc-page{
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

.sb {
  margin-top: 2vw;
}

</style>
