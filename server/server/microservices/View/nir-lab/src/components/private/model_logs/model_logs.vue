<template>
  <div class="model_logs-page">
    <div>
      <Menu page="/model_logs" name="brais"/>
    </div>
    <h1>Model Logs</h1>

    <div id="select_model">
      <div class="border_rect">
        <p>
          <br /><br>
        </p>
      </div>
    </div>

    <div id="info_samples">
      <div id="information">

      </div>
      <div id="upload_model">
        <div class="border_rect">
          <p>
            <br /><br>
          </p>
        </div>
      </div>
      <div id="samples">
        
      </div>    
    </div>
    

    <div>
      <Footer />
    </div>
  </div>
</template>

<script>
import Menu from "../../common/header/private/menu.vue";
import Footer from "../../common/footer/footer.vue";

export default {
  data: () => ({
    models: [
      {
        name: "model_1",
        download_link: "model1.h5",
        problem: "HadaBeer",
        target: "Graduation",
        train_size: 54,
        train_acc: "75%",
        test_size: 13,
        test_acc: 84
      }, 
      {
        name: "model_2",
        download_link: "model2.h5",
        problem: "HadaBeer",
        target: "Graduation",
        train_size: 57,
        train_acc: "75%",
        test_size: 16,
        test_acc: 88
      }, 
      {
        name: "model_3",
        download_link: "model3.h5",
        problem: "HadaBeer",
        target: "Graduation",
        train_size: 54,
        train_acc: "75%",
        test_size: 13,
        test_acc: 84
      }],
      results: [
        {
          sample: "1",
          prediction: "0"
        },
        {
          sample: "2",
          prediction: "0"
        },
        {
          sample: "3",
          prediction: "1"
        }
      ]

    
  }),
  methods: {
    getImgDb() {
      return require("/src/assets/private/data_treatment/data_base.png");
    }
  },
  components: {
    Menu,
    Footer,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.model_logs-page {
  background-color: #deeaee;
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

#select_model {
  width: 100%;
  padding: 0vw;
}

.border_rect {
  width: 95%;
}

#info_samples {
  width: 100%;
}

#information {
  width: 50%;
  float: left;
}

#upload_model {
  width: 20%;
}

#samples {
  width: 30%;
  float: right;
}


</style>
