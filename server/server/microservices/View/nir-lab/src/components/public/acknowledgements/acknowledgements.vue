<template>
  <div class="acknowledgements-page">
    <div>
      <Menu page='/acknowledgements'/>
    </div>
    <h1>Acknowledgements</h1>

    <div class="images_div">
        <a v-for="(image, idx) in images" :key="idx"  :href="image.url">
          <b-img class="images_ack" :src="getImgUrl(image.src)" :alt="image.name"/>
        </a>
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
.acknowledgements-page {
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

.images_div {
  text-align: center;
}

.images_ack {
  height: 15vw;
  width: 15vw;
  padding: 3vw;
  position: relative;
  border-radius: 0.15em;
  text-align: center;
}


</style>
