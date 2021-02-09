<template>
  <v-footer
    dark
    padless
  >
    <v-card
      flat
      tile
      class="lighten-1 white--text text-center"
      v-bind="colored"
    >
      <v-avatar v-for="(image, idx) in images" :key="idx">
        <a class="images_logo" :href="image.url">
          <b-img :src="getImgUrl(image.src)" :alt="image.name"/>
        </a>
      </v-avatar>

      <p class="footer_text white--text pt-0">
        {{  text  }}
      </p>

      <v-divider></v-divider>

      <v-card-text class="white--text">
        {{ new Date().getFullYear() }} — <strong>NIR-Lab</strong>
      </v-card-text>
    </v-card>
  </v-footer>
</template>

<script>
  import { colorAccent3 } from '/src/globalStyle.js'
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
        text: 'The server that has been developed at the RNASA-IMEDIR laboratory provides ways to perform an exhaustive treatment of the data as well as a simple and visual development of different models capable of estimating results about the data collected by the NIR-Lab application available for download on GitHub.',
        position: 'fixed',
        color: 'color'
    
    }
  },
  methods: {
    getImgUrl(imgSrc) {
      return require('/src/assets/common/footer/' + imgSrc);
    }
  },
  computed: {
      positioning () {
        const attrs = {}
        attrs[this.position] = true  
        return attrs
      },
      colored () {
        const attrs = {}
        attrs[this.color] = colorAccent3  
        return attrs
      },
    },
}
  // v-bind="positioning"
</script>

<style>
.images_logo {
  height: 2vw;
  width: 2vw;
  margin: 1vw;
}

.theme--dark.v-footer {
  position: absolute;
  bottom: 0;
  width: 100%;
}

.footer_text {
  font-size: 0.75vw;
  margin-left: 5vw;
  margin-right: 5vw;
}

</style>