<template>
  <div class="contributers-page">
    <div>
      <Menu page='/contributers'/>
    </div>
    <h1>Contributers</h1>

    <div class="border_rect">
      <p>
        The main contributors to this project have been:
        <ul>
          <li>
            <strong>RNASA-IMEDIR research group</strong>: this is the research group in which the development 
            of this project has been carried out.
            <br><br>
            The RNASA-IMEDIR research group is the group in which this project has been developed. This group has numerous milestones in fields such as machine learning since its foundation.
            It has many diverse lines of research and is composed of 37 researchers.
          </li>
        </ul>
      </p>
    </div>
    
    <div class='images_contrib_div'>
      <b-img class="images_cont" :src="require('/src/assets/common/footer/rnasa_imedir.png')" :alt="RNASA-IMEDIR"/>
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
            src: 'rnasa_imedir.png',
            name: 'RNASA-IMEDIR',
            url: 'http://rnasa-imedir.udc.es'
          },
          {
            src: 'github.png',
            name: 'GitHub',
            url: 'https://github.com/braiscgaldo/NIR-Lab-2.0'
          },
          {
            src: 'twitter.png',
            name: 'Twitter',
            url: 'https://twitter.com/rnasa_imedir'
          },
          {
            src: 'udc.png',
            name: 'UDC',
            url: 'https://www.udc.es/es/'
          },
          {
            src: 'fic.png',
            name: 'FIC',
            url: 'https://www.fic.udc.es/'
          }
        ],    
    }
  },
  methods: {
    getImgUrl(imgSrc) {
      return require('/src/assets/common/footer/' + imgSrc);
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
.contributers-page {
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
  margin-bottom: 3vw;
  text-align: center;
}

.images_cont {
  position: relative;
  margin: 1vw;
}

.images_contrib_div {
  text-align: center;
}



</style>
