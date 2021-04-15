<template>
  <div v-if="this.$store['state']['user'] != ''" class="logs-page">
    <div>
      <Menu page="/logs" name="brais" />
    </div>
    <h1>Logs</h1>

    <div id="select_database">
      <h2>Databases</h2>
      <div id="database_area" class="border_rect">
        <div class="container" v-for="(db, idx) in databases" :key="idx">
          <div :id="db.name" class="database" @dblclick="downloadFile">
            {{ db.name }}
          </div>
        </div>
      </div>
    </div>

    <div id="select_model">
      <h2>Models</h2>
      <div id="models_area" class="border_rect">
        <div class="container" v-for="(model, idx) in models" :key="idx">
          <div :id="model.name" class="model" v-on:click="clickedModel($event)" @dblclick="downloadFile">
            {{ model.name }}
          </div>
        </div>
      </div>
    </div>

    <div id="info_samples">
      <div id="information">
        <label><h2>Information</h2></label>
        <vue-table-dynamic
          :params="table_model"
          id="table_info"
        ></vue-table-dynamic>
      </div>
      <div id="samples">
        <label><h2>Results</h2></label>
        <vue-table-dynamic
          :params="table_results"
          id="table_results"
        ></vue-table-dynamic>
      </div>
    </div>

    <label><h2>Results</h2></label>
    <div id="upload_model" class="border_rect"  ref='file' accept=".json" @drop.prevent='uploadData' @dragover.prevent>
      <b-img class="data_image" :src="getImgData()" alt="Model image"></b-img>
    </div>

    <div>
      <Footer />
    </div>
  </div>
  <div v-else>
    <div>
      <Menu page="/data_treatment"/>
    </div>
    <h1>BAD ACCESS 403</h1>
    <div>
      <Footer />
    </div>
  </div>
</template>

<script>
import Menu from "../../common/header/private/menu.vue";
import Footer from "../../common/footer/footer.vue";
const axioslib = require('axios');
const axios = axioslib.create({
  headers: {
    'Access-Control-Allow-Origin': '*'
  }
});

export default {
  data: () => ({
    databases : [
    ],
    database_selected: "",
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
  created() {
    this.obtainFiles();
  },
  methods: {
    // Obtain model data
    obtainModelData(modelName){
      axios.get('http://localhost:4000/download', { params:{ username: this.$store['state']['user'], path: 'models/'+modelName+'.json' } }).then(response => {
        if (response.status == 200 && response.data['file_content'] != 'File not exists'){
          var data = JSON.parse(response.data['file_content']);
          console.log(data)
          for (var i = 0; i < this.models.length; i++){
            if (this.models[i].name == modelName){
              for (var j = 0; j < Object.keys(data).length; j++){
                this.models[i][Object.keys(data)[j]] = data[Object.keys(data)[j]]
              }
              break;
            }
          }
          
        } else {
          console.log('File not exists')
        }
        console.log(this.database_names)
      })
    },
    // Obtain files
    obtainFiles() {
      axios.get('http://localhost:4000/list_files', { params:{ username: this.$store['state']['user'] } }).then(response => {
        if (response.status == 200 && response.data['message'] == 'files returned'){
          var db = response.data['files']['databases'][1];
          var mdls = response.data['files']['models'][1];
          this.databases = []; this.models = [];
          for (var i = 0; i < db.length; i++){
            this.databases.push({ name: db[i].slice(0, -5) });
          }
          for (i = 0; i < mdls.length; i++){
            var name = (mdls[i].length - mdls[i].indexOf('.') == 3) ? mdls[i].slice(0, mdls[i].indexOf('.')) : null;
            if (name != null){
              this.models.push({ name: name });
              console.log(mdls[i].slice(0, mdls[i].indexOf('.')))
              this.obtainModelData(name);
            }
          }
        } else {
          console.log('bad return')
        }
      })
    },
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
      
    },
    // Download files
    downloadFile(event){
      var target = event.target;
      var targetId = target.id;
      var targetClass = target.className;
      var targetPath = (targetClass == 'database') ? 'databases/'+targetId+'.json' : 'models/'+targetId+'.h5';
      console.log(target)
      console.log(targetId, targetClass);
      console.log(targetPath)
      axios.get('http://localhost:4000/download', { params:{ username: this.$store['state']['user'], path: targetPath } }).then(response => {
        if (response.status == 200){
          var data = response.data['file_content'];
          var type = response.data['file_type'] 
          const blob = new Blob([data], {type: 'text/plain'})
          const e = document.createEvent('MouseEvents'),
          a = document.createElement('a');
          a.download = targetId + type;
          a.href = window.URL.createObjectURL(blob);
          a.dataset.downloadurl = ['text/json', a.download, a.href].join(':');
          e.initEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
          a.dispatchEvent(e);
        } else {
          console.log('bad return')
        }
        console.log(this.database_names)
      })
    },
    // Upload data for predictions
    uploadData(e){
      e.preventDefault();
      this.$refs.file.file = e.dataTransfer.files[0];
      this.database_files = e.dataTransfer.items;
      if (!this.database_files || this.database_files.length > 1) return;

      if (this.$refs.file.file.name.endsWith('.json')){
        var reader = new FileReader();
        reader.readAsText(this.database_files[0].getAsFile());
        reader.onloadend = event => {
          var data = {
            type: 'AddFile',
            username: this.$store['state']['user'],
            filedata: event.target.result,
            path: 'predict/' + this.$refs.file.file.name 
          }
          axios.put('http://localhost:4000/', data).then(response => {
            if (response.status == 200){
              console.log('uploaded data, updating view');
              this.predict()
            }
          })
        }
      }
    },
    // predict
    predict(){
      var data = {
        type: 'Predict',
        data_file: '/home/' + this.$store['state']['user'] + '/predict/' + this.$refs.file.file.name,
        model_name: '/home/' + this.$store['state']['user'] + '/models/' + 'model_1.h5'
      };
      axios.post('http://localhost:4000/', data).then(response => {
        if (response.status == 202){
           console.log('predicting results...') 
        }
      })
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
.logs-page {
  background-color: #deeaee;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

h1,
h2,
h3 {
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

#select_model,
#select_database {
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
  margin: inherit;
  float: left;
  position: relative;
  height: auto;
  display: block;
}

.model {
  padding: 5px;
  margin: 5px;
  margin-left: 0%;
  margin-right: 0%;
  background-color: #ee3744;
  border: solid 1px #ee3744;
  color: #deeaee;
  border-radius: 0.75em;
  text-align: justify;
  text-justify: inter-word;
  font-size: 1.5vw;
  width: fit-content;
}

.database {
  padding: 5px;
  margin: 5px;
  margin-left: 0%;
  margin-right: 0%;
  background-color: #2fa2a2;
  border: solid 1px #2fa2a2;
  color: #deeaee;
  border-radius: 0.75em;
  text-align: justify;
  text-justify: inter-word;
  font-size: 1.5vw;
  width: fit-content;
}
</style>
