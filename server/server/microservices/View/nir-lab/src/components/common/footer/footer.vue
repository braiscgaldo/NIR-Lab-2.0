<template>
  <v-footer
    dark
    padless
    v-bind="positioning"
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

      <v-card-text class="accent2 white--text pt-0">
        {{  text  }}
      </v-card-text>

      <v-divider></v-divider>

      <v-card-text class="white--text">
        {{ new Date().getFullYear() }} — <strong>NIR-Lab</strong>
      </v-card-text>
    </v-card>
  </v-footer>
</template>

<script>
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
        text: 'El servidor que ha sido desarrollado en el laboratorio RNASA-IMEDIR proporciona formas de realizar un tratamiento exhaustivo de los datos además de un desarrollo sencillo y visual de diferentes modelos capaces de estimar resultados acerca de los datos.',
        position: 'fixed',
        color: 'color'
    
    }
  },
  methods: {
    getImgUrl(imgSrc) {
      console.log(imgSrc);
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
        attrs[this.color] = '#5C5E46'   
        return attrs
      },
    },
}
  
</script>

<style>
.images_logo {
  height: 3vw;
  width: 3vw;
  margin: 1vw;
}



</style>