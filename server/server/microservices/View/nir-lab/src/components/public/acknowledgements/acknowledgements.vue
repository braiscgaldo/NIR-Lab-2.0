<template>
  <div class="acknowledgements-page">
    <div v-if="this.$store['state']['user'] == ''">
      <MenuPub page='/acknowledgements'/>
    </div>
    <div v-else>
      <MenuPriv page='/acknowledgements'/>
    </div>

    <div class="border_rect">
      <p class='title_info'><strong>Acknowledgements</strong></p>
      <p class='info_text'>
        <ul>
          <li>
            The <strong>RNASA-IMEDIR</strong> research group is the group in which this project has been developed. This group has numerous milestones in fields such as machine learning since its foundation.
            <br>
            It has many diverse lines of research and is composed of 37 researchers.
          </li>
          <li>
            The <strong>UDC</strong> is the public university where the project is carried out and to which the RNASA-IMEDIR research group belongs.
            <br>
            It currently consists of 2 large campuses: A Coruña and Ferrol and has 39 degrees as well as numerous masters and doctoral programs.
          </li>
          <li>
            The <strong>FIC</strong> is the faculty in which the project has been developed. It belongs to the A Coruña campus of the UDC and is where the RNASA-IMEDIR research group is located.
            <br>
            This faculty offers the Degree in Computer Engineering and the Degree in Data Science as well as numerous doctoral and master's degree programs, including the Master's Degree in Computer Engineering.
          </li>
        </ul>
      </p>
        <p class='spam_info'>Visit them by clicking in the correspondent logo!</p>
    </div>

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
import MenuPub from "../../common/header/public/menu.vue"
import MenuPriv from "../../common/header/private/menu.vue"
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
    MenuPub,
    MenuPriv, 
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
