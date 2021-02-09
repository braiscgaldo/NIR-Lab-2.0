<template>
  <div class="front-page">
    <div>
      <Menu page='/'/>
    </div>
    <h1>NIR LAB</h1>
    <p>
      A server for analyze samples of NIRScan Nano EVM.
    </p>

    <div id='front_image'>
      <b-img :src="require('/src/assets/front_page.png')" alt="NIR Lab Spectrophotometer"></b-img>
    </div>
    
    <p>
      Follow us in our social networks!!
    </p>

    <div id="images">
      <v-avatar rounded size="128" v-for="(image, idx) in images" :key="idx">
        <a class="images_logo" :href="image.url">
          <b-img :src="getImgUrl(image.src)" :alt="image.name"/>
        </a>
      </v-avatar>
    </div>
    
    <div>
      <Footer/>
    </div>
  </div>
</template>

<script>
import Menu from "./common/header/public/menu.vue"
import Footer from "./common/footer/footer.vue";

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
  margin-top: 4vw;
  text-align: center;
}

h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #FFF;
}

p {
  text-align: center;
}

.v-avatar{
  align-items: center;
  margin-right: 5vw;
  margin-left: 5vw;
}

.images_logo {
  height: 7vw;
  width: 7vw;
}

#images{
  text-align: center;
}

#front_image {
  text-align: center;
}

</style>
