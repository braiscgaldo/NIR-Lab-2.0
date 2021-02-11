<template>
  <div class="model_logs-page">
    <div>
      <Menu page="/model_logs" name="brais"/>
    </div>
    <h1>Model Logs</h1>

    <div id="select_model">
      <div id="models_area" class="border_rect">
        <div class="container" v-for="(model, idx) in models" :key="idx">
          <div :id="model.name" class="model" v-on:click="clickedModel($event)">
            {{  model.name  }}          
          </div>
        </div>
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
        test_acc: 84,
        selected: true
      }, 
      {
        name: "model_2",
        download_link: "model2.h5",
        problem: "HadaBeer",
        target: "Graduation",
        train_size: 57,
        train_acc: "75%",
        test_size: 16,
        test_acc: 88,
        selected: true
      }, 
      {
        name: "model_3",
        download_link: "model3.h5",
        problem: "HadaBeer",
        target: "Graduation",
        train_size: 54,
        train_acc: "75%",
        test_size: 13,
        test_acc: 84,
        selected: true
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
      ],
        header: 'row',
        border: true,
        stripe: true,
        showCheck: true,
        sort: [0, 1, 2, 3, 4, 5, 6],
        columnWidth: [{column: 3, width: '13%'}, {column: 4, width: '13%'}, {column: 5, width: '13%'}, {column: 6, width: '13%'}]
      
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
    },
    loadModels(){
      // load models
    },
    filterTable(row, modelId){
      //Filter the table
      return row[0] != modelId;
    },
    clickedModel(event){
      var modelId = event.currentTarget.id;
      var model;

      // Test if data is in to delete it
      var length = this.table_model.data.length; 
      this.table_model.data = this.table_model.data.filter(row => this.filterTable(row, modelId));
      if (length > this.table_model.data.length){
        return;
      }

      // Obtain the correspondent model
      for (var idx = 0; idx < this.models.length; idx++){
        if (this.models[idx].name === modelId){
          model = this.models[idx];
          break;
        }
      }
      var data = [model.name, model.problem, model.target, model.train_size, model.train_acc, model.test_size, model.test_acc];
      this.table_model.data.push(data)
      
    }
  },
  mounted(){
    this.loadModels()
  },
  components: {
    Menu,
    Footer,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.model_logs-page {
  background-color: #deeaee;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

h1, h2, h3 {
  margin-top: 4vw;
  margin-bottom: 3vw;
  text-align: center;
}

h3 {
  width: 100%;
  padding-left: 7vw;
  padding-top: 0vw;
}

label {
  width: 100% !important;
}

#select_model {
  width: 100%;
  padding: 0vw;
}

.border_rect {
  width: 99%;
  overflow-x: auto;
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

.container {
  max-width: fit-content;
  margin: 1%;
  float: left;
  position: relative;
  height: auto;
  display: block;
}

.model {
  padding:5px;
  margin:5px;
  margin-left: 0%;
  margin-right: 0%;
  background-color: #EE3744;
  border: solid 1px #EE3744;
  color: #deeaee;
  border-radius: 0.75em;
  text-align: justify;
  text-justify: inter-word;
  font-size: 1.5vw;
  width: fit-content;
}

</style>
