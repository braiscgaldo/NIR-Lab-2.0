<template>
  <div class="front-page">
    <div>
      <Menu page='/mobile_app'/>
    </div>
    <h1>Mobile Application</h1>

    <div class="border_rect" id='introduction'>
      <p>
        This section will explain the use of the NIR-Lab mobile application. The operation of the 
        application is based on a series of <strong>Problems</strong> containing <strong>Measurements</strong>. 
        To start using the application, the user creates a problem and gives it a name and labels that can be 
        numerical or categorical. The user will then perform a measurement and will be able to store the results 
        in this problem. Each of the results of the problem is known as a measurement.<br><br> 
        
        This information is stored in a file in the JSON format, which is the most appropriate given its simplicity 
        and dictionary-like appearance.<br><br>
        The user can also enter a machine learning model that has the format .h5 and make predictions about 
        the measurement data. <br><br>

        The 2 most important use cases of the application will be explained below:
      </p>
    </div>

    <div class="part_info">
      <b-img class="images_mob_app_r" :src="getImgUrl(images[0].src)" :alt="images[0].name"/>
      <div class="border_rect" id='left_rect'>
        <h2>Create a Problem</h2>
        <p>
          Once the application is open, click on the problem button and then on the add button. Once these tasks 
          have been completed, you can proceed to define the problem by giving it a name and the labels that the 
          user wants with the appropriate nature of the labels.
        </p>
      </div>
    </div>
 
    <div class="part_info">
      <b-img class="images_mob_app_l" :src="getImgUrl(images[1].src)" :alt="images[1].name"/>
      <div class="border_rect" id='right_rect'>
        <h2>Take a measure and perform a prediction</h2>
        <p>
          To perform a scan, it is necessary to open the application and connect to the spectrophotometer and then press 
          the scan button. Once pressed, the problem in question must be selected. The measurement is then performed and 
          the store button is pressed. The sample is labeled and stored. <br><br>

          If you want to make an estimate about the data, click on the predict button and select the corresponding model. 
          Once this task is done, you can see the result offered by the selected model.
        </p>
      </div>
    </div>

    <div class="border_rect" id='end'>
      <p>
        This explains the use of the cell phone application. Once a problem has been generated, simply access 
        the file and upload it to the server. Once this task has been completed, the data can be processed prior 
        to training in order to obtain good estimates of the data.
      </p>
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
  data() {
    return {
        images: [
          {
            src: 'crear_problema.png',
            name: 'Crear un Problema'
          },
          {
            src: 'medicion_prediccion.png',
            name: 'Realizar una Medición y una Predicción'
          }
        ],    
    }
  },
  methods: {
    getImgUrl(imgSrc) {
      return require('/src/assets/public/information/' + imgSrc);
    }
  },
  components: {
    Menu, 
    Footer
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.front-page{
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

h2 {
  font-size: 1.7vw;
  font: bold;
  text-align: center;
}

#introduction {
  font-size: 1.2vw;
  text-align: justify;
  text-justify: inter-word;
}

#left_rect {
  font-size: 1.1vw;
  width: 50%;
  float: left;
  margin: auto;
  vertical-align: middle;
  margin-top: 20vw;
  margin-left: 5vw;
  text-justify: inter-word;
}

#right_rect {
  font-size: 1.1vw;
  width: 50%;
  float: right;
  margin: auto;
  vertical-align: middle;
  margin-top: 20vw;
  margin-right: 5vw;
  text-justify: inter-word;
}

.border_rect {
  margin-top: 1vw;
}

.images_mob_app_r{
  float: right;
  margin-bottom: 10vw; 
  max-height: 50vw;
  margin-right: 10vw;
}

.images_mob_app_l{
  float: left;
  margin: auto;
  max-height: 60vw;
  margin-left: 10vw;
}

.part_info {
  height: 50vw;
}

#end {
  margin-top: 15vw;
  margin-bottom: 5vw;
  font-size: 1.2vw;
  text-align: justify;
  text-justify: inter-word;
}

</style>
