<template>
  <div class="editAcc-page">
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
        <input class="sb" type="submit" value="Edit Account">
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
import Menu from "../../common/header/private/menu.vue"
import Footer from "../../common/footer/footer.vue";

export default {
  data: () => ({
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
    edit() {
      console.log(this.username);
      console.log(this.password);
      console.log(this.repeated_password);
      console.log(this.name);
      console.log(this.surname);
      console.log(this.email);      
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
