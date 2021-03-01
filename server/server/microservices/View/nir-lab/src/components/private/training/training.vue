<template>
  <div class="training-page">
    <div>
      <Menu page="/training" name="brais" />
    </div>
    <h1>Training</h1>

    <!-- Train menu -->
    <div id="train_menu">
      <form class="vue-form train_form" @submit.prevent="train">
        <fieldset>
          <input class="train_button" type="submit" value="Train" />
          <v-combobox
            class="combo_select_db"
            required
            v-model="database_selected"
            :items="database_names"
            placeholder="Select a database"
          >
          </v-combobox>
          <v-combobox
            class="combo_select_model"
            required
            v-model="model_selected"
            :items="model_names"
            placeholder="Select a model"
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
        <form class="vue-form sel_db_form" @submit.prevent="deleteDB">
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
            <div class="drop-zone">
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
          @submit.prevent="generateNewConfigFile()"
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
                  @dblclick="deleteLayer"
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
                @dblclick="showModalEditActivation=true; id_layer=layer.id"
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
                @dblclick="showModalEditValue=true; id_layer=layer.id"
              >
                {{ Object.keys(layer)[3] + ": " }}
                {{ layer[Object.keys(layer)[3]] }}
              </div>
            <div class="images_drop">
                <b-img
                  class="key_img"
                  :src="getImgKey()"
                  alt="Key image"
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
// dialogs
import VueModal from '@kouts/vue-modal';

export default {
  data: () => ({
    database_selected: "",
    database_names: ["HadaBeer", "Chocolate"],
    model_selected: "",
    config_parameters: {
      model_name: "",
      learning_rate: "learning_rate",
      target_selected: "",
      target: ["Graduation", "intensity"],
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
    model_names: ["model_1", "model_2"],
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
        name: "AveragePool1D",
        activation: "",
        parameters: ["pool_size"]
      },
    ],
    configuration_file_rows: 0,
    showModalEditValue: false,
    showModalErrorEditValue: false,
    showModalEditActivation: false,
    new_value: '32',
    id_layer: '',
    new_activation: 'relu',
    configuration_file_layers_id: [
      {
        name: "Dense",
        id: 0,
        activation: "relu",
        units: "35"
      },
    ],
  }),
  methods: {
    getImgNN() {
      return require("/src/assets/private/training/neural_net.png");
    },
    getImgKey() {
      return require("/src/assets/private/data_treatment/key_right.png");
    },
    getImgArrow() {
      return require("/src/assets/private/training/arrow_right.png");
    },
    adminImagesDropArea() {
      var id, idparam, idact = null;
      for (var i = 0; i < this.configuration_file_layers_id.length; i++) {
        id = this.configuration_file_layers_id[i].name + '$' + i;
        console.log('ident', id)
        this.configuration_file_layers_id[i].id = i;
        idparam = id + '$';
        idact = id + '$activation';
        document.getElementById(id).id = this.configuration_file_layers_id[i].name + '$' + i;
        document.getElementById(idact).innerHTML = 'activation: ' + this.configuration_file_layers_id[i].activation;
        if (document.getElementById(id + '$') != null) document.getElementById(id + '$').id = idparam;
        document.getElementById(idparam).innerHTML = Object.keys(this.configuration_file_layers_id[i])[3] + ':' +  this.configuration_file_layers_id[i][Object.keys(this.configuration_file_layers_id[i])[3]];
       }
       console.log(this.configuration_file_layers_id)
    },
    deleteLayer(event){
      var id = event.target.id.substring(event.target.id.indexOf('$') + 1);
      if (this.configuration_file_layers_id.length == 1 ) return null;   
      this.configuration_file_layers_id.splice(id, 1);
      this.adminImagesDropArea()
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
        case 'AveragePool1D':
          return 4;
      }
    },
    onDrop(){
      var nameLayer = null;
      for(var i=0; i < this.configuration_file_layers_id.length; i++){
        nameLayer = this.configuration_file_layers_id[i].name;
        if (Object.keys(this.configuration_file_layers_id[i]).includes('parameters')){
          this.configuration_file_layers_id[i] = { name: nameLayer, id: i, activation: 'relu' };
          this.configuration_file_layers_id[i][this.layers[this.matchLayer(nameLayer)].parameters[0]] = this.new_value;
        }
      }
      this.adminImagesDropArea();
    },
    changeValuesInEdition(index){
      console.log(index, index, Object.keys(this.configuration_file_layers_id[index])[3] + ':'+ this.new_value)

      var id = this.configuration_file_layers_id[index].name + '$' + index + '$';
      // change values in script
      this.configuration_file_layers_id[index][Object.keys(this.configuration_file_layers_id[index])[3]] = this.new_value;
      // change values in html
      document.getElementById(id).innerHTML = Object.keys(this.configuration_file_layers_id[index])[3] + ': ' + this.new_value;
    },
    editValue(){
      if (this.new_value == '' || !this.new_value.match("^[0-9]+$")) {
        this.showModalErrorEditValue = true;
      }else{
        for (var i=0; i < this.configuration_file_layers_id.length; i++){
          if (this.configuration_file_layers_id[i].id == this.id_layer) this.changeValuesInEdition(i);
        }
        this.showModalEditValue = false;
      }
    },
    changeActivationInEdition(index){
      console.log(index, index, Object.keys(this.configuration_file_layers_id[index])[3] + ':'+ this.new_value)

      var id = this.configuration_file_layers_id[index].name + '$' + index + '$activation';
      // change values in script
      this.configuration_file_layers_id[index].activation = this.new_activation;
      // change values in html
      console.log(id, 'activation:', this.new_activation, document.getElementById(id))
      document.getElementById(id).innerHTML = 'activation: ' + this.new_activation;
    },
    editActivation(){
      for (var i=0; i < this.configuration_file_layers_id.length; i++){
        if (this.configuration_file_layers_id[i].id == this.id_layer) this.changeActivationInEdition(i);
      }
      this.showModalEditActivation = false;
      
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
  width: 45%;
  float: left;
}

.combo_select_model {
  width: 45%;
  float: right;
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

@media screen and (max-width: 1300px){
  #config_parameters, #admin_models, #layers_names, #palette {
    width: 99%;
  }

  .layer, .layer_drop, .parameter_drop {
    font-size: 2.5vw;
  }

  h2 {
    font-size: 3.5vw;
  }

  h1 {
    font-size: 4vw;
  }

  h3 {
    font-size: 3vw;
  }

  .key_img, .key_img_left {
    width: 2.25vw;
  }

  .cf_button {
    width: 40vw;
    font-size: 2vw;
  }

  .vue-form input[type="submit"] {
    float: inherit;
    width: auto;
  }

}

@media screen and (max-width: 1018px){
  #config_parameters, #admin_models, #layers_names, #palette {
    width: 99%;
  }

  .layer, .layer_drop, .parameter_drop {
    font-size: 2vw;
  }

  h2 {
    font-size: 3.5vw;
  }

  h1 {
    font-size: 4vw;
  }

  h3 {
    font-size: 3vw;
  }

  .key_img, .key_img_left {
    width: 2vw;
  }

  .cf_button {
    width: 40vw;
    font-size: 2vw;
  }

  .vue-form input[type="submit"] {
    float: inherit;
    width: auto;
  }

}

@media screen and (max-width: 750px){
  #config_parameters, #admin_models, #layers_names, #palette {
    width: 99%;
  }

  .layer, .layer_drop, .parameter_drop {
    font-size: 2vw;
  }

  h2 {
    font-size: 3.5vw;
  }

  h1 {
    font-size: 4vw;
  }

  h3 {
    font-size: 3vw;
  }

  .key_img, .key_img_left {
    width: 2.25vw;
  }

  .cf_button {
    width: 40vw;
    font-size: 2vw;
  }

  .vue-form input[type="submit"] {
    float: inherit;
    width: auto;
  }

}

@media screen and (max-width: 500px){
  #config_parameters, #admin_models, #layers_names, #palette {
    width: 99%;
  }

  .layer {
    font-size: 2.5vw;
  }

  h2 {
    font-size: 3.5vw;
  }

  h1 {
    font-size: 4vw;
  }

  h3 {
    font-size: 3vw;
  }

  .key_img, .key_img_left {
    width: 3vw;
  }

  .cf_button {
    width: 40vw;
    font-size: 2vw;
  }

  .vue-form input[type="submit"] {
    float: inherit;
    width: auto;
  }

}

</style>
