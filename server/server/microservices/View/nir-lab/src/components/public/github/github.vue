<template>
  <div class="github-page">
    <div>
      <Menu page='/github'/>
    </div>
    <h1>Github</h1>

    <div class="border_rect">
      <p>
        This project has a repository on GitHub where the code of this system and 
        the implemented application for the smartphone with Android operating system are stored. All the 
        modules required for its installation and use are included, both for the mobile application and 
        for the training server.<br><br>

        The mobile application was implemented in Java using Android Studio. The server backend was 
        implemented in Python whereas the front-end was implemented in Vue JS using JS, HTML5 and CSS 
        functionalities.
      </p>
      <a :href="image.url">
          <b-img class="images_github" :src="getImgUrl(image.src)" :alt="image.name"/>
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
        image: 
          {
            src: 'github.png',
            name: 'GitHub',
            url: 'https://github.com/braiscgaldo/NIR-Lab-2.0'
          },    
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
.github-page{
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
  margin-bottom: 3vw;
  text-align: center;
}


.images_github {
  margin-left: auto;
  margin-right: auto;
  display: block;
}

</style>
