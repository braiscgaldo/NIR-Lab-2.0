<template>
  <div class="information-page">
    <div v-if="this.$store['state']['user'] == ''">
      <MenuPub page='/github'/>
    </div>
    <div v-else>
      <MenuPriv page='/github'/>
    </div>
    <h1>Information</h1>

    <div class="border_rect">
      <p class="info_text">
        This server is part of the NIR-Lab collection. This collection consists of the mobile application 
        that can be used with the Texas Instruments NIRScan Nano EVM spectrophotometer. This application is 
        available on GitHub along with the code for this server. To learn about the use of the Android application 
        can be found by clicking here.<br><br>

        As for the server, it should be noted that it has been developed using a microservices-based architecture. 
        The back-end part has been developed entirely in Python while the front-end has been developed in VUE JS.<br><br>

        As for the different modules that make up the system, the following can be highlighted:<br><br>
        <ul>
          <li>
            <strong>Data Processing Module</strong>. It is a module belonging to the back-end. It is in charge of performing 
            transformations on the selected data and generating new data sets.
          </li>
          <li>
            <strong>Training Module</strong>. It is a module in charge of generating a model and training it with the data 
            selected by the user. It is implemented using TensorFlow.
          </li>
          <li>
            <strong>Facade Module</strong>. It is in charge of carrying out the authentication in the server (the possibility 
            of implementing a separate module for this task has been studied but it has been seen that it is not so necessary
             at this time) and serves as an interface against the other services and to obtain relevant information about the 
             different data and users.
          </li>
          <li>
            <strong>View module</strong>. It is the system view. To access it, a browser is used. Its main tasks are to offer 
            a correct and simple visualization to the user to perform his data processing and model training tasks. It is in 
            charge of sending requests to the back-end so that it can carry out the tasks requested by the user.
          </li>
        </ul>

        This server is in continuous development adding new functionalities, especially in relation to the model configuration 
        and to the extension of the operations on the data provided by the user.
      </p>
    </div>
 
    <div>
      <Footer/>
    </div>
  </div>
</template>

<script>
import MenuPub from "../../common/header/public/menu.vue"
import MenuPriv from "../../common/header/private/menu.vue"
import Footer from "../../common/footer/footer.vue";

export default {
  components: {
    MenuPub,
    MenuPriv, 
    Footer
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.information-page{
  background-color: #DEEAEE;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

h1 {
  margin-top: 3vw;
  text-align: center;
}

.info_text {
  font-size: 1.2vw;
  text-align: justify;
  text-justify: inter-word;
}

</style>
