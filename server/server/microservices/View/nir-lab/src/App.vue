<template>
  <v-app>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import { colorForeground } from '/src/globalStyle.js'

export default {
  name: 'App',

  data: () => ({
    //
    color: 'color'
  }),
  computed: {      
    colored () {
        const attrs = {}
        console.log(colorForeground)
        attrs[this.color] = colorForeground  
        return attrs
      }
  }
};
</script>

<style>
.title_page {
    position: relative;
    width: 100%;
}

.title_page h1 {
  width: 100%;
  text-align: center;
}


</style>