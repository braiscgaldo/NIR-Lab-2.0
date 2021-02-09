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
        <label><h2>Information</h2></label>
        <vue-table-dynamic :params="table_model" id="table_info"></vue-table-dynamic>
      </div>
      <div id="samples">
        <label><h2>Results</h2></label>
        <vue-table-dynamic :params="table_results" id="table_results"></vue-table-dynamic>
      </div>    
    </div>


    <label><h2>Results</h2></label>
    <div id="upload_model" class="border_rect">
      <b-img
        class="data_image"
        :src="getImgData()"
        alt="Model image"
      ></b-img>
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
      },
    ],
    table_model: {
      data: [
        ['Model name', 'Problem', 'Target', 'Train size', 'Train acc', 'Test size', 'Test acc'],
        ['model_1', 'HadaBeer', 'Graduation', '57', '80', '20', '75'],
        ['model_2', 'HadaBeer', 'Graduation', '52', '85', '20', '79'],
        ['model_3', 'HadaBeer', 'Graduation', '53', '84', '20', '75'],
      ],
        header: 'row',
        border: true,
        stripe: true,
        showCheck: true,
        sort: [0, 1, 2, 3, 4, 5, 6],
        columnWidth: [{column: 0, width: '10%'}]
      
    },
    table_results: {
      data: [
        ['Sample', 'Prediction'],
        ['1', '0'],
        ['2', '1'],
        ['3', '1'],
      ],
        header: 'row',
        border: true,
        stripe: true,
        showCheck: true,
        sort: [0, 1]
      
    }    
  }),
  methods: {
    getImgData() {
      return require("/src/assets/private/model_logs/data.png");
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

h1, h2 {
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
  overflow: hidden;
  width: 100%;
  padding: 0vw;
}

#information {
  width: 70%;
  float: left;
}

#table_info {
  width: 90%;
  float: left;
  margin: 2vw;
}

#upload_model {
  width: 97%;
  text-align: center;
  margin: 2vw;
}

.border_rect {
  margin-right: 0vw;
}

.data_image {
  width: 5vw;
  text-align: center;
}

#samples {
  width: 30%;
  float: right;
}

#table_results {
  width: 90%;
  margin: 2vw;
}



</style>
