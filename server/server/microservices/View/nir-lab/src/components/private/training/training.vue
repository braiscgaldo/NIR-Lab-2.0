<template>
  <div class="training-page">
    <div>
      <Menu page="/training" username="brais" />
    </div>
    <h1>Training</h1>

    <!-- Train menu -->
    <div id="train_menu">
      <form class="vue-form train_form" @submit.prevent="trainModel">
        <fieldset>
          <input class="train_button" type="submit" value="Train" />
          <v-combobox
            class="combo_select_db"
            required
            v-model="database_selected"
            :items="database_names"
            placeholder="Select a database"
            @change="obtainLabels"
          >
          </v-combobox>
          <v-combobox
              class="combo_target"
              required
              placeholder="Target"
              name="target selected"
              id="target_selected"
              :items="target"
              v-model="target_selected"
          />
          <v-combobox
            class="combo_select_model"
            required
            v-model="model_selected"
            :items="config_model_names"
            placeholder="Select a configuration model file"
          >
          </v-combobox>
        </fieldset>
      </form>
    </div>

    <!-- Configuration parameters / Admin models -->
    <div id="config_admin">
      <!-- Configuration parameters -->
      <div id="config_parameters">
        <form class="vue-form config_parameters_form">
          <fieldset>
            <label><h2>Configuration Parameters</h2></label>
            <input
              type="text"
              placeholder="Model name"
              name="model_name_input"
              id="model_name_input"
              required
              v-model="config_parameters.model_name"
            />
            <input
              type="number"
              step="0.001"
              name="learning_rate_input"
              id="learning_rate_input"
              v-model="config_parameters.learning_rate"
              placeholder="Learning rate"
              controls
            />
            <input
              type="number"
              step="1"
              required
              name="epochs_input"
              id="epochs_input"
              v-model="config_parameters.epochs"
              placeholder="Epochs"
              controls
            />
            <input
              type="number"
              step="0.001"
              name="epsilon_input"
              id="epsilon_input"
              v-model="config_parameters.epsilon"
              placeholder="Epsilon"
              controls
            />
            <input
              type="number"
              step="1"
              required
              name="batch_size_input"
              id="batch_size_input"
              v-model="config_parameters.batch_size"
              placeholder="Batch size"
              controls
            />
            <input
              type="number"
              step="0.001"
              name="decay_input"
              id="decay_input"
              v-model="config_parameters.decay"
              placeholder="Decay"
              controls
            />
            <input
              type="number"
              step="1"
              required
              name="train_size_input"
              id="train_size_input"
              v-model="config_parameters.train_size"
              placeholder="Train size"
              controls
            />
            <input
              type="number"
              step="0.001"
              name="validation_size_input"
              id="validation_size_input"
              v-model="config_parameters.validation_size"
              placeholder="Validation size"
              controls
            />
            <v-combobox
              class="target_input"
              required
              name="target_input"
              v-model="config_parameters.target_selected"
              :items="config_parameters.target"
              placeholder="Select the target"
            >
            </v-combobox>
            <v-combobox
              class="optimizer_input"
              required
              name="optimizer_input"
              v-model="config_parameters.optimizer_selected"
              :items="config_parameters.optimizer"
              placeholder="Select the optimizer"
            >
            </v-combobox>
            <v-combobox
              class="loss_input"
              required
              name="loss_input"
              v-model="config_parameters.loss_selected"
              :items="config_parameters.loss"
              placeholder="Select the loss function"
            >
            </v-combobox>
            <v-combobox
              class="metrics_input"
              required
              name="metrics_input"
              v-model="config_parameters.metrics_selected"
              :items="config_parameters.metrics"
              placeholder="Select the metric"
            >
            </v-combobox>
          </fieldset>
        </form>
      </div>

      <!-- Admin models -->
      <div id="admin_models">
        <form class="vue-form sel_db_form" @submit.prevent="deleteModel">
          <fieldset>
            <input
              class="delete_button"
              type="submit"
              value="Delete the model"
            />
            <v-combobox
              class="combo_models"
              required
              v-model="model_selected"
              :items="model_names"
              placeholder="Select a model"
            >
            </v-combobox>
            <div class="drop-zone" ref='file' accept=".h5" @drop.prevent='uploadModel' @dragover.prevent>
              <b-img
                class="db_image"
                :src="getImgNN()"
                alt="Model image"
              ></b-img>
            </div>
          </fieldset>
        </form>

        <div id="layers">
          <label><h3>Layers</h3></label>
          <div class="border_rect" id="layers_names">
            <draggable
              :list="layers"
              class="list-group layer_group"
              :move="onDrop"
              ghost-class="ghost"
              :group="{ name: 'layers', pull: 'clone', put: false }"
            >
              <div class="container" v-for="(layer, idx) in layers" :key="idx">
                <div :id="layer.name" class="layer">
                  {{ layer.name }}
                </div>
              </div>
            </draggable>
          </div>
        </div>
      </div>
    </div>

    <!-- Desing Palette -->
    <div id="design_palette">
      <div id="palette">
        <h2>Design Palette</h2>
        <form
          class="vue-form palette_form"
          @submit.prevent="generateConfigFile"
        >
          <fieldset>
            <input
              class="cf_button"
              type="submit"
              value="Generate config file"
            />
          </fieldset>
        </form>
        <!--  :move="adminImagesDropArea" -->
        <div class="border_rect" id="layers_palette">
          <div id="layers_id">

            <div>
            <draggable
              :list="configuration_file_layers_id"
              class="list-group"
              ghost-class="ghost"
              :group="{ name: 'layers', pull: 'clone' }"
              @change="onDrop"
            >
              <div
                v-for="(layer, idx) in configuration_file_layers_id"
                :key="idx"
                class="layer_container"
              >
                <div
                  :id="layer.name + '$' + idx"
                  class="layer_drop"
                >
                  {{ layer.name }}
                </div>

              <div class="images_drop">
                  <b-img
                    class="key_img key_img_left"
                    :src="getImgKey()"
                    alt="Key image"
                  ></b-img>
              </div>

              <div
                :id="layer.name + '$' + idx + '$activation'"
                class="parameter_drop"
                @dblclick="showModalEditActivation=true; id_layer = idx;"
              >
                {{ 'activation: ' + layer.activation }}
              </div>
              <VueModal v-model="showModalEditActivation" title="Introduce the correspondant number!">
                <div>
                  <v-combobox
                    :id="layer.name + '$' + layer.id + '$activation$Dialog'"
                    class="combo_models"
                    required
                    v-model="new_activation"
                    :items="activations"
                    placeholder="Select an activation"
                  >
                  </v-combobox>
                </div>
                <div>
                  <button class="edit_activation_dialog_button" @click="editActivation">Edit activation</button>
                </div>
              </VueModal>

              <div
                :id="layer.name + '$' + idx + '$'"
                class="parameter_drop"
                @dblclick="showModalEditValue = true; id_layer = idx;"
              >
                {{ Object.keys(layer)[2] + ": " }}
                {{ layer[Object.keys(layer)[2]] }}
              </div>
            <div class="images_drop">
                <b-img
                  class="key_img"
                  :src="getImgKey()"
                  alt="Key image"
                ></b-img>
            </div>
            <div class="images_drop">
                <b-img
                  class="trash_img"
                  :src="getImgTrash()"
                  alt="Trash image"
                  @click="del_layer_idx=idx; deleteLayer()"
                ></b-img>
            </div>
              <VueModal v-model="showModalEditValue" title="Introduce the correspondant number!">
                <div>
                  <input class="add_param_input" type="text" name="value" :id="layer.name + '$' + layer.id + '$' + Object.keys(layer)[3] + '$Dialog'" required v-model="new_value" :placeholder="layer[Object.keys(layer)[3]]">
                </div>
                <div>
                  <button class="add_param_dialog_button" @click="editValue">Edit value</button>
                </div>
              </VueModal>
              <VueModal v-model="showModalErrorEditValue" title="Error editting value">
                <p>
                  The variable must not be empty and contain a number.
                </p>
              </VueModal>
            </div>

          </draggable>

              </div>
          </div>
        </div>
        <VueModal v-model="showModalErrorConfigIntegrity" title="Error treating Inputs">
        <p>
          The configuration file does not pass integrity, it is bad composed. Please, fill all information!
        </p>
      </VueModal>
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
import draggable from "vuedraggable";
const axioslib = require('axios');
const axios = axioslib.create({
  headers: {
    'Access-Control-Allow-Origin': '*'
  }
});
axios.defaults.headers.post['Content-Type'] = 'application/json';
// dialogs
import VueModal from '@kouts/vue-modal';

export default {
  data: () => ({
    database_selected: "",
    database_names: [],
    model_names: [],
    model_selected: "",
    target_selected: "",
    target: [],
    config_parameters: {
      model_name: "",
      learning_rate: "learning_rate",
      epsilon: "Epsilon",
      epochs: "Epochs",
      optimizer_selected: "",
      optimizer: [
        "Adam",
        "Adamax",
        "SGD",
        "RMSprop",
        "Adadelta",
        "Adagrad",
        "Adamax",
        "Nadam",
        "Ftrl",
      ],
      loss_selected: "",
      loss: ["binary_crossentropy", "sparse_categorical_crossentropy"],
      metrics: ["acc"],
      batch_size: "Batch size",
      decay: "Decay",
      train_size: "Train size",
      validation_size: "Validation size",
    },
    activations: ['relu', 'sigmoid', 'softmax', 'softplus', 'softsign', 'tanh', 'selu', 'elu', 'exponential'],
    config_model_names: ["model_1", "model_2"],
    del_layer_idx: '',
    layers: [
      {
        name: "Dense",
        activation: "",
        parameters: ["units"]
      },
      {
        name: "Dropout",
        activation: "",
        parameters: ["rate"] 
      },
      {
        name: "Softmax",
        activation: "",
        parameters: ["none"]
      },
      {
        name: "MaxPool1D",
        activation: "",
        parameters: ["pool_size"]
      },
      {
        name: "AveragePooling1D",
        activation: "",
        parameters: ["pool_size"]
      },
    ],
    configuration_file_rows: 0,
    showModalEditValue: false,
    showModalErrorEditValue: false,
    showModalEditActivation: false,
    showModalErrorConfigIntegrity: false,
    new_value: '32',
    id_layer: '',
    new_activation: 'relu',
    configuration_file_layers_id: [
      {
        name: "Dense",
        activation: "relu",
        units: "35"
      },
    ],
  }),
  created () {
    this.obtainFiles();
    console.log('change')
  }, 
  methods: {
    // Obtain files
    obtainFiles() {
      axios.get('http://localhost:4000/list_files', { params:{ username: this.$store['state']['user'] } }).then(response => {
        if (response.status == 200 && response.data['message'] == 'files returned'){
          this.database_names = response.data['files']['databases'][1];
          this.model_names = response.data['files']['models'][1];
          this.config_model_names = response.data['files']['models_config'][1];
          this.database_names.forEach(function(part, index, db_names) { db_names[index] = db_names[index].slice(0,-5);});
          this.config_model_names.forEach(function(part, index, db_names) { db_names[index] = db_names[index].slice(0,-5);});
          this.model_names.forEach(function(part, index, db_names) { db_names[index] = db_names[index].slice(0,-3);});
        } else {
          console.log('bad return')
        }
        console.log(this.database_names)
      })
    },
    // Obtain labels
    obtainLabels(){
      axios.get('http://localhost:4000/list_characteristics', { params:{ username: this.$store['state']['user'], filename: this.database_selected } }).then(response => {
        if (response.status == 200){
          this.target = response['data']['characteristics']['labels'];
        } else {
          console.log('bad return ')
        }
      })
    },
    getImgNN() {
      return require("/src/assets/private/training/neural_net.png");
    },
    getImgKey() {
      return require("/src/assets/private/data_treatment/key_right.png");
    },
    getImgArrow() {
      return require("/src/assets/private/training/arrow_right.png");
    },
    getImgTrash() {
      return require("/src/assets/private/training/trash.png");
    },
    adminImagesDropArea() {
      var id, idparam, idact = null;
      for (var i = 0; i < this.configuration_file_layers_id.length; i++) {
        id = this.configuration_file_layers_id[i].name + '$' + i;
        idparam = id + '$';
        idact = id + '$activation';
        if (document.getElementById(id) != null){
          document.getElementById(id).id = this.configuration_file_layers_id[i].name + '$' + i;
          document.getElementById(idact).innerHTML = 'activation: ' + this.configuration_file_layers_id[i].activation;
          document.getElementById(id + '$').id = idparam;
          document.getElementById(idparam).innerHTML = Object.keys(this.configuration_file_layers_id[i])[2] + ':' +  this.configuration_file_layers_id[i][Object.keys(this.configuration_file_layers_id[i])[2]];
        }
       }
    },
    deleteLayer(){
      if (this.configuration_file_layers_id.length == 1) return null;
      var id, idact, idparam = null;
      for (var i = 0; i < this.configuration_file_layers_id.length-1; i++){
        console.log(this.configuration_file_layers_id[i].name)
        id = this.configuration_file_layers_id[i].name + '$' + i;
        idparam = id + '$';
        idact = id + '$activation';
        document.getElementById(idact).innerHTML = 'activation: ' + this.configuration_file_layers_id[i+1].activation;
        document.getElementById(idparam).innerHTML = Object.keys(this.configuration_file_layers_id[i+1])[2] + ':' +  this.configuration_file_layers_id[i+1][Object.keys(this.configuration_file_layers_id[i+1])[2]];
      }
      this.configuration_file_layers_id.splice(this.del_layer_idx, 1);
    },
    matchLayer(layer){
      switch (layer) {
        case 'Dense':
          return 0;
        case 'Dropout':
          return 1;
        case 'Softmax':
          return 2;
        case 'MaxPool1D':
          return 3;
        case 'AveragePooling1D':
          return 4;
      }
    },
    onDrop(event){
      if (event.added != null){ 
        var nameLayer = this.configuration_file_layers_id[event.added.newIndex].name;
        if (Object.keys(this.configuration_file_layers_id[event.added.newIndex]).includes('parameters')){
          this.configuration_file_layers_id[event.added.newIndex] = { name: nameLayer, activation: 'relu' };
          this.configuration_file_layers_id[event.added.newIndex][this.layers[this.matchLayer(nameLayer)].parameters[0]] = this.new_value;
        }
      }
      this.adminImagesDropArea();
    },
    editValue(){
      if (this.new_value == '' || !this.new_value.match("^[0-9]+$")) {
        this.showModalErrorEditValue = true;
      }else{
        this.configuration_file_layers_id[this.id_layer][Object.keys(this.configuration_file_layers_id[this.id_layer])[2]] = this.new_value;
        var id = this.configuration_file_layers_id[this.id_layer].name + '$' + this.id_layer + '$';
        document.getElementById(id).innerHTML = Object.keys(this.configuration_file_layers_id[this.id_layer])[2] + ': ' + this.new_value;
        this.showModalEditValue = false;
      }
    },
    editActivation(){
      this.configuration_file_layers_id[this.id_layer].activation = this.new_activation;
      var id = this.configuration_file_layers_id[this.id_layer].name + '$' + this.id_layer + '$activation';
      // change values in html
      document.getElementById(id).innerHTML = 'activation: ' + this.new_activation;
      this.showModalEditActivation = false;
    },
    // treat models
    deleteModel(){
      axios.delete('http://localhost:4000/delete_file', { params:{ username: this.$store['state']['user'], path: 'models/' + this.model_selected + '.h5' } }).then(response => {
        if (response.status == 200){
          this.obtainFiles();
          this.model_selected = '';
        }
      })
    },
    uploadModel(e){
      e.preventDefault();
      this.$refs.file.file = e.dataTransfer.files[0];
      this.database_files = e.dataTransfer.items;
      if (!this.database_files || this.database_files.length > 1) return;

      if (this.$refs.file.file.name.endsWith('.h5')){
        var reader = new FileReader();
        reader.readAsText(this.database_files[0].getAsFile());
        reader.onloadend = event => {
          var data = {
            type: 'AddFile',
            username: this.$store['state']['user'],
            filedata: event.target.result,
            path: 'models/' + this.$refs.file.file.name 
          }
          axios.put('http://localhost:4000/', data).then(response => {
            if (response.status == 200){
              console.log('uploaded data, updating view');
              this.obtainFiles();
            }
          })
        }
      }
    }, 
    testJSON(){
      var valid = true;
      (this.config_parameters.model_name == '' || this.config_parameters.target_selected == '' || this.config_parameters.num_epochs == 'Epochs' || this.config_parameters.loss_selected == '' || this.config_parameters.learning_rate == 'learning_rate' || this.config_parameters.epsilon == 'Epsilon' || this.config_parameters.optimizer_selected == '' || this.config_parameters.metrics == '' || this.config_parameters.batch_size == 'Batch size' || this.config_parameters.decay == 'Decay' || this.config_parameters.train_size == 'Train size' || this.config_parameters.validation_size == 'Validation size') ? valid = false : valid = true;
      console.log(this.config_parameters.model_name == '' || this.config_parameters.target_selected == '' || this.config_parameters.num_epochs == 'Epochs' || this.config_parameters.loss_selected == '' || this.config_parameters.learning_rate == 'learning_rate' || this.config_parameters.epsilon == 'Epsilon' || this.config_parameters.optimizer_selected == '' || this.config_parameters.metrics == '' || this.config_parameters.batch_size == 'Batch size' || this.config_parameters.decay == 'Decay' || this.config_parameters.train_size == 'Train size' || this.config_parameters.validation_size == 'Validation size')
      return valid;    
    },
    // Configuration file
    generateConfigJSON(){
      var configJSON = {
        name: this.config_parameters.model_name,
        target: this.config_parameters.target_selected,
        num_epochs: this.config_parameters.epochs,
        loss_function: this.config_parameters.loss_selected,
        learning_rate: this.config_parameters.learning_rate,
        epsilon: this.config_parameters.epsilon,
        optimizer: this.config_parameters.optimizer_selected.toLowerCase(),
        metrics: this.config_parameters.metrics,
        batch_size: this.config_parameters.batch_size,
        decay: this.config_parameters.decay,
        train_size: this.config_parameters.train_size,
        validation_size: this.config_parameters.validation_size,
        test_size: 1-this.config_parameters.train_size,
        layers: {}
      };
      for (var i = 0; i < this.configuration_file_layers_id.length; i++){
          configJSON['layers'][i] = this.configuration_file_layers_id[i];
      }

      for (i = 0; i < Object.keys(configJSON['layers']).length; i++){
        console.log(Object.keys(configJSON['layers'][i]));
        if (Object.keys(configJSON['layers'][i]).indexOf('none') > 1) {
          delete configJSON['layers'][i].none;
        }
      }

      console.log(configJSON);
      return configJSON;
    },
    // Generate configuration file
    generateConfigFile(){
      if (this.testJSON()){
        var configData = JSON.stringify(this.generateConfigJSON());
        var data = {
          type: 'AddFile',
          username: this.$store['state']['user'],
          filedata: configData,
          path: 'models_config/' + this.config_parameters.model_name + '_config.json' 
        }
        console.log(data);
        
        axios.put('http://localhost:4000/', data).then(response => {
          if (response.status == 200){
            console.log('uploaded data, updating view');
            this.showModalGenerateConfigFile = true;
            this.obtainFiles();
          }
        });
        return;
      }
      this.showModalErrorConfigIntegrity = true;
    },
    // Train model
    trainModel(){
      var data = {
        type: 'Training',
        path_preprocessed_database: '/home/' + this.$store['state']['user'] + '/databases/' + this.database_selected + '.json',
        path_configuration_model: '/home/' + this.$store['state']['user'] + '/models_config/' + this.origin_database_selected + '.json',
        target_label: this.target_selected
      }
      axios.post('http://localhost:4000/', data).then(response => {
        if (response.status == 202){
          console.log('executing background task');
          this.showModalGenerateDB = true;
        }
      })
      return data;
    }
  },
  components: {
    Menu,
    Footer,
    draggable,
    VueModal
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.training-page {
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

.delete_button {
  background-color: red !important;
  margin-top: 1vw;
  margin-left: 1vw;
}

label {
  width: 100% !important;
}

#train_menu {
  width: 100%;
}

.train_form {
  width: 95%;
  margin-top: 2vw;
}

.combo_select_db {
  width: 30%;
  float: left;
}

.combo_select_model {
  width: 30%;
  float: left;
}

.combo_target {
  width: 30%;
  float: left;
}

.train_button {
  margin: 0.75vw;
}

#config_admin {
  width: 100%;
}

#config_parameters {
  width: 60%;
  float: left;
}

.combo_parameters_left,
#model_name_input,
.target_input,
#epochs_input,
.loss_input,
#batch_size_input,
#train_size_input {
  width: 48%;
  float: left;
  margin-right: 1vw;
}

.combo_parameters_right,
#learning_rate_input,
#epsilon_input,
.optimizer_input,
.metrics_input,
#decay_input,
#validation_size_input {
  width: 48%;
  float: right;
}

.config_parameters_form {
  width: 92%;
}

#admin_models {
  width: 40%;
  float: right;
}

#design_palette {
  margin-top: auto !important;
}

#palette {
  width: 100%;
}

#layers {
  width: 100%;
  float: right;
}

#layers_names {
  float: right;
  padding: 0%;
  max-height: fit-content;
}

.border_rect {
  width: 90%;
  margin-left: 2vw;
}

.palette_form {
  width: 20%;
  padding: 0vw;
}

.cf_button {
  width: 100%;
  margin: 0;
  float: left;
}

/*******************  Layers image ****************************/

.container {
  max-width: fit-content;
  margin: inherit;
  float: left;
  position: relative;
  height: auto;
  display: block;
}

.layer {
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

.layer_group {
  display: contents;
}

.layer_container {
  position: relative;
  display: flex;
}

.layer_drop {
  padding: 5px;
  margin: 5px;
  background-color: #ee3744;
  border: solid 1px #ee3744;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
  width: 40%;
}

#layers_id {
  width: 100%;
}

.key_img {
    margin: 0px;
    width: 1.35vw;
    margin-bottom: 0.18vw;
}

.trash_img {
width: 1vw;
margin-left: 1vw;
margin-right: 1vw;
margin-top: 1.15vw;
margin-bottom: 1.15vw;
}

.key_img_left {
    width: 1.35vw; 
    margin: 0px;
    margin-left: 4vw;
    margin-bottom: 0.18vw;
    transform: rotate(180deg);
}

.images_drop {
  position: relative;
  padding: 0;
}

#layers_palette {
  position: relative;
  display: inline-flex;
}

.parameter_drop {
  padding: 5px;
  margin: 5px;
  background-color: #2fa2a2;
  border: solid 1px #2fa2a2;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
  width: 20%;
}

.activation_id {
  padding: 5px;
  margin: 5px;
  margin-right: 0%;
  background-color: #2fa2a2;
  border: solid 1px #2fa2a2;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
}

.parameter_id {
  padding: 5px;
  margin: 5px;
  margin-right: 0%;
  background-color: #2fa2a2;
  border: solid 1px #2fa2a2;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
}

.add_param_input {
  width: 100%;
  border: 1px solid #ee3744;
  border-radius: 0.75em;
}

.vm-title {
  padding-top: 2vw;
  font-size: 1.85vw;
}

.vm-titlebar {
  display: inline-block;
  align-items: start;
}

.vm-content {
  text-align: center;
  contain: layout;
}

.edit_activation_dialog_button, .add_param_dialog_button{
  margin-top: 1vw;
  border: none;
  background: #33262D;
  border-radius: 0.25em;
  padding: 12px 20px;
  color: #DEEAEE;
  font-weight: bold;
  float: right;
  cursor: pointer;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  appearance: none;
}


@media screen and (max-width: 1408px){
  #config_parameters, #admin_models, #layers_names, #palette {
    display: none;
  }

  .combo_select_db, .combo_select_model {
    width: 99%;
    float: left;
  }



  .vue-form input[type="submit"] {
    float: inherit;
    width: 98.5%;
  }

  footer {
    position: fixed;
  }

}

</style>
