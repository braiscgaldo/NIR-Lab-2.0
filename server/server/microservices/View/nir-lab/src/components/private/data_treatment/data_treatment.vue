<template>
  <div v-if="this.$store['state']['user'] != ''" class="data_treatment-page">
    <div>
      <Menu page="/data_treatment"/>
    </div>
    <h1>Data Treatment</h1>

    <!-- Selects and geners -->
    <div id="gen_sel">
      <!-- Generate new database div -->
      <div id="gen_new_db">
        <form class="vue-form gen_db_form" @submit.prevent="generateNewDB">
          <fieldset>
            <label>Generate a database!</label>
            <v-combobox
              class="combo_config"
              required
              placeholder="Database name"
              name="database_to_generate_name"
              id="database_selected"
              :items="databases_names"
              v-model="origin_database_selected"
            />
            <v-combobox
              class="combo_config"
              required
              v-model="configuration_file_selected"
              :items="configuration_files"
              placeholder="Select a configuration file"
            >
            </v-combobox>
            <input
              class="action_button"
              type="submit"
              value="Generate new database"
            />
          </fieldset>
        </form>
      </div>
      <VueModal v-model="showModalGenerateDB" title="Generating Database!">
        <p>
          Generating new Database!!
          <br><br>
          It will be available when its process ends!!
        </p>
      </VueModal>

      <!-- Add / select a database -->
      <div id="select_db">
        <form class="vue-form sel_db_form" @submit.prevent="deleteDB">
          <fieldset>
            <input
              class="delete_button"
              type="submit"
              value="Delete the database"
            />
            <label>Select a database</label>
            <v-combobox
              class="combo_database"
              required
              v-model="database_selected"
              :items="databases_names"
              placeholder="Select a database"
              @change="obtainCharacteristicsFromDB"
            >
            </v-combobox>
            <div class="drop-zone" ref='file' accept=".json" @drop.prevent='uploadDB' @dragover.prevent>
              <b-img
                class="db_image"
                :src="getImgDb()"
                alt="Database image"
              ></b-img>
            </div>
          </fieldset>
        </form>
      </div>
      <VueModal v-model="showModalUploadDataBase" title="Uploading Database...">
        <p>
          Uploading database......
        </p>
      </VueModal>
    </div>

    <h2>Design Palette</h2>
    <!-- Desing Palette -->
    <div id="design_palette">
      <div id="palette">
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
            <input
              type="text"
              placeholder="Configuration file name"
              name="configuration_file_to_generate_name"
              id="configuration_file_to_generate_name"
              required
              v-model="configuration_file_to_generate_name"
            />
          </fieldset>
        </form>
        <VueModal v-model="showModalGenerateConfigFile" title="Generating Configuration File">
          <p>
            The config file is generating....
          </p>
        </VueModal>
        <div class="border_rect">
          <div id="var_id">
            <draggable
              :list="configuration_file_opper_apply"
              class="list-group"
              ghost-class="ghost"
              :move="adminImagesDropArea"
              :group="{ name: 'operations', pull: 'clone'}"
              @change="adminImagesDropArea"
            >
              <div
                id="opper_apply"
                v-for="(operation, idx) in configuration_file_opper_apply"
                :key="idx"
              >
                <div :id="'opper_apply$' + operation.name + '$' + idx" class="operation_drop">
                  {{ operation.abreviation }}
                </div>
              </div>
            </draggable>
            <div class="images_drop">
              <div v-for="index in configuration_file_rows" :key="index">
                <b-img
                  class="key_left_img"
                  :src="getImgKey()"
                  alt="Key Left image"
                ></b-img>
             </div>
            </div>
            <draggable
              :list="configuration_file_var_1"
              class="list-group"
              ghost-class="ghost"
              :group="{ name: 'inputs', pull: 'clone'}"
              @change="adminImagesDropArea"
            >
              <div
                id="var_1"
                v-for="(Input, idx) in configuration_file_var_1"
                :key="idx"
              >
                <div v-if="isConstant(Input.name) && isOneParamVar1(idx)" :id="'var_1$' + Input.name + '$' + idx" class="constant var_1_one_param" @dblclick="showModalEditConstantVar1 = true; id_click = idx;">
                  {{ Input.name }}
                </div>
                <div v-else-if="isConstant(Input.name)" :id="'var_1$' + Input.name + '$' + idx" class="constant" @dblclick="showModalEditConstantVar1 = true; id_click = idx;">
                  {{ Input.name }}
                </div>
                <div v-else-if="isOneParamVar1(idx)" :id="'var_1$' + Input.name + '$' + idx" class="input_drop var_1_one_param" @dblclick="deleteVars">
                  {{ Input.name }}
                </div>
                <div v-else :id="'var_1$' + Input.name + '$' + idx" class="input_drop" @dblclick="deleteVars">
                  {{ Input.name }}
                </div>

                <VueModal v-model="showModalEditConstantVar1" title="Introduce the new value to constant!">
                  <div>
                    <input class="add_param_input" type="text" name="value" :id="'var_1$' + Input.name + '$' + idx + '$Dialog'" required v-model="new_value" placeholder="Nuevo valor">
                  </div>
                  <div>
                    <button class="add_param_dialog_button" @click="editConstantVar1">Edit constant</button>
                  </div>
                </VueModal>
              </div>
            </draggable>
            <draggable
              :list="configuration_file_var_2"
              class="list-group"
              ghost-class="ghost"
              :group="{ name: 'inputs', pull: 'clone'}"
              @change="adminImagesDropArea"
            >
              <div
                id="var_2"
                v-for="(Input, idx) in configuration_file_var_2"
                :key="idx"
              > 
                <div v-if="isConstant(Input.name)" :id="'var_2$' + Input.name + '$' + idx" class="constant" @dblclick="showModalEditConstantVar2 = true; id_click = idx;">
                  {{ Input.name }}
                </div>
                <div v-else-if="isHidden(Input.name)" :id="'var_2$' + Input.name + '$' + idx" class="input_drop" style="visibility:hidden">
                  {{ Input.name }}
                </div>
                <div v-else :id="'var_2$' + Input.name + '$' + idx" class="input_drop" @dblclick="deleteVars">
                  {{ Input.name }}
                </div>
                <VueModal v-model="showModalEditConstantVar2" title="Introduce the new value to constant!">
                  <div>
                    <input class="add_param_input" type="text" name="value" :id="'var_1$' + Input.name + '$' + idx + '$Dialog'" required v-model="new_value" placeholder="Nuevo valor">
                  </div>
                  <div>
                    <button class="add_param_dialog_button" @click="editConstantVar2">Edit constant</button>
                  </div>
                </VueModal>
              </div>
              <VueModal v-model="showModalErrorDrag" title="Error adding variables to palette">
              <p>
                Please, fill previous items first.
              </p>
            </VueModal>
            </draggable>
            <div class="images_drop">
              <div v-for="index in configuration_file_rows" :key="index">
                <b-img
                  class="key_img"
                  :src="getImgKey()"
                  alt="Key image"
                ></b-img>
              </div>
            </div>
            <div class="images_drop">
              <div v-for="index in configuration_file_rows" :key="index">
                <b-img
                  class="arrow_img"
                  :src="getImgArrow()"
                  alt="Arrow image"
                ></b-img>
              </div>
            </div>
            <draggable
              :list="configuration_file_var_id"
              class="list-group"
              ghost-class="ghost"
              :move="adminImagesDropArea"
              :group="{ name: 'inputs', pull: 'clone'}"
              @change="adminImagesDropArea"
            >
              <div
                id="var_container"
                v-for="(Input, idx) in configuration_file_var_id"
                :key="idx"
              >
                <div :id="'var_id$' + Input.name + '$' + idx" class="input_drop">
                  {{ Input.name }}
                </div>
              </div>
            </draggable>
            <div class="images_drop">
              <div v-for="index in configuration_file_rows" :key="index">
                <b-img
                  :id="'trash$' + index"
                  class="trash_img"
                  :src="getImgTrash()"
                  alt="Trash image"
                  @click="deleteRow"
                ></b-img>
              </div>
            </div>
          </div>
        </div>
      </div>
      <VueModal v-model="showModalErrorConfigIntegrity" title="Error treating Input">
        <p>
          The configuration file does not pass integrity, it is bad composed.
        </p>
      </VueModal>
      <div id="inputs">
        <h3 id="operations_title">Operations</h3>
        <h3 id="var_title">Inputs</h3>
        <h3 id="output_title">Output</h3>

        <div class="border_rect" id="var_names">
          <draggable
              style="content-visibility: auto;"
              :list="inputs"
              v-bind="{ghostClass: 'ghost'}"
              :group="{ name: 'inputs', pull: 'clone', put: false }"
            >
            <div
              class="container container_var"
              id="var_container"
              v-for="(Input, idx) in inputs"
              :key="idx"
            >
             <!-- otro color cuando sea output -->
            <toggle-button :value="Input.is_output"
                color="#82C7EB"
                :sync="true"
                :labels="{checked: 'Out', unchecked: 'None'}"
                :id="Input.name_tb"
                class="toggle_button"
                v-model="Input.is_output"
                />
                
              
                <div v-if="Input.is_output" :id="Input.name" class="Input Input-inside-output" @dblclick="showModalEditVar=true; new_var=Input.name; edit_var=new_var">
                  {{ Input.name }}
                </div>
                <div v-else :id="Input.name" class="Input Input-inside" @dblclick="showModalEditVar=true; new_var=Input.name; edit_var=new_var">
                  {{ Input.name }}
                </div>
                <VueModal v-model="showModalEditVar" title="Edit the name of the Input!">
                <div>
                  <input class="add_input_input" type="text" name="Input" id="edit_var" required v-model="new_var" :placeholder="new_var">
                </div>
                <div>
                  <button class="add_input_dialog_button" @click="editinput">Edit Input</button>
                </div>
              </VueModal>
            </div>
          </draggable>

          <button class="add_input_button"  @click="showModalAddVar=true; new_var=''">+</button>
          <VueModal v-model="showModalAddVar" title="Add a name to the Input!">
            <div>
              <input class="add_input_input" type="text" name="Input" id="new_var" required v-model="new_var" placeholder="Input name">
            </div>
            <div>
              <button class="add_input_dialog_button" @click="addNewinput">Add New Input</button>
            </div>
          </VueModal>
          <VueModal v-model="showModalErrorAddVar" title="Error treating Input">
            <p>
              The Input must not be empty or must not contain any symbol except characters or numbers.
              <br><br>
              It also must not must be declared in other inputs.
              <br><br>
              Its length must not raise 20 characters.
              <br><br>
              The Input called 'none' cannot be edit.
            </p>
          </VueModal>
        </div>

        <div class="border_rect" id="operation_names">
          <draggable
            style="content-visibility: auto;"
            :list="operations"
            v-bind="{ghostClass: 'ghost'}"
            :group="{ name: 'operations', pull: 'clone', put: false }"
          >
            <div
              class="container container_op"
              v-for="(operation, idx) in operations"
              :key="idx"
            >
              <div :id="operation.name" class="operation">
                {{ operation.abreviation }}
              </div>
            </div>
          </draggable>
        </div>
      </div>
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
  setToken(token) {
    window.localStorage.setItem('jwt_token', token);
  },
  getToken() {
    return window.localStorage.getItem('jwt_token');
  },
  data: () => ({
    // for generating new db parting from files
    origin_database_selected: "",
    configuration_file_selected: "",
    configuration_files: [],
    // for delete // generate databases
    database_selected: "",
    databases_names: [],
    // files to upload
    database_files: [],
    database_data: '',
    // for generate config -- DESINGN PALETTE
    configuration_file_to_generate_name: "",
    showModalAddVar: false,
    showModalErrorAddVar: false,
    showModalEditVar: false,
    showModalErrorDrag: false,
    showModalEditConstantVar1: false,
    showModalEditConstantVar2: false,
    showModalGenerateDB: false,
    showModalErrorConfigIntegrity: false,
    showModalGenerateConfigFile: false,
    showModalUploadDataBase: false,
    new_var: '',
    new_value: '',
    id_click: '',
    edit_var: '',
    inputs: [
      {
        name: "intensity",
        name_tb: "intensity_tb",
        is_output: false,
      },
      {
        name: "absorbance",
        name_tb: "absorbance_tb",
        is_output: true,
      },
      {
        name: "reflectance",
        name_tb: "reflectance_tb",
        is_output: false,
      },
    ],
    operations: [
      {
        name: "sum",
        abreviation: "sum",
        num_parameters: 2,
      },
      {
        name: "subtract",
        abreviation: "subtract",
        num_parameters: 2,
      },
      {
        name: "multiply",
        abreviation: "multiply",
        num_parameters: 2,
      },
      {
        name: "divide",
        abreviation: "divide",
        num_parameters: 2,
      },
      {
        name: "module",
        abreviation: "module",
        num_parameters: 2,
      },
      {
        name: "whole_divide",
        abreviation: "whole_div",
        num_parameters: 2,
      },
      {
        name: "square_root",
        abreviation: "sqrt",
        num_parameters: 1,
      },
      {
        name: "raise_x",
        abreviation: "raise_x",
        num_parameters: 2,
      },
      {
        name: "standard_deviation",
        abreviation: "std",
        num_parameters: 1,
      },
      {
        name: "mean",
        abreviation: "mean",
        num_parameters: 1,
      },
      {
        name: "median",
        abreviation: "median",
        num_parameters: 1,
      },
      {
        name: "mode",
        abreviation: "mode",
        num_parameters: 1,
      },
      {
        name: "sum_all",
        abreviation: "sum_all",
        num_parameters: 1,
      },
      {
        name: "minimum",
        abreviation: "min",
        num_parameters: 1,
      },
      {
        name: "maximum",
        abreviation: "max",
        num_parameters: 1,
      },
      {
        name: "min_max_normalization",
        abreviation: "min_max_n",
        num_parameters: 1,
      },
      {
        name: "normalize",
        abreviation: "normalize",
        num_parameters: 1,
      },
      {
        name: "variance",
        abreviation: "variance",
        num_parameters: 1,
      },
      {
        name: "max_abs_normalization",
        abreviation: "max_abs_n",
        num_parameters: 1,
      },
      {
        name: "value_at",
        abreviation: "value_at",
        num_parameters: 2,
      },
    ],
    display: "Clone",
    dragging: true,
    original_inputs: ["intensity", "absorbance", "reflectance"],
    configuration_file_var_id: [
      {
        name: "intensity",
        name_tb: "intensity_tb",
        is_output: false,
      }
    ],
    configuration_file_opper_apply: [
      {
        name: "sum",
        abreviation: "sum",
        num_parameters: 2,
      }
    ],
    configuration_file_var_1: [
      {
        name: "reflectance",
        name_tb: "reflectance_tb",
        is_output: false,
      }
    ],
    configuration_file_var_2: [
      {
        name: "absorbance",
        name_tb: "absorbance_tb",
        is_output: true
      }
    ],
    configuration_file_rows: 1
  }),
  created() {
    console.log('obtain files for user ' + this.$store['state']['user']);
    this.obtainFiles();
  },
  methods: {
    // on create methods
    obtainFiles() {
      axios.get('http://localhost:4000/list_files', { params:{ username: this.$store['state']['user'] } }).then(response => {
        if (response.status == 200 && response.data['message'] == 'files returned'){
          this.databases_names = response.data['files']['databases'][1];
          this.configuration_files = response.data['files']['databases_config'][1];
          this.databases_names.forEach(function(part, index, db_names) { db_names[index] = db_names[index].slice(0,-5);});
          this.configuration_files.forEach(function(part, index, db_names) { db_names[index] = db_names[index].slice(0,-5);});
        } else {
          console.log('bad return')
        }
      })
    },
    // Obtain images
    getImgDb() {
      return require("/src/assets/private/data_treatment/data_base.png");
    },
    getImgArrow() {
      return require("/src/assets/private/data_treatment/arrow_right.png");
    },
    getImgKey() {
      return require("/src/assets/private/data_treatment/key_right.png");
    },
    getImgTrash() {
      return require("/src/assets/private/data_treatment/trash.png");
    },
    // db treatment
    generateNewDB() {
      var data = {
        type: 'DataTreatment',
        configuration_name: '/home/' + this.$store['state']['user'] + '/databases_config/' + this.configuration_file_selected + '.json',
        file_data_name: '/home/' + this.$store['state']['user'] + '/databases/' + this.origin_database_selected + '.json'
      }
      axios.post('http://localhost:4000/', data).then(response => {
        if (response.status == 202){
          console.log('executing background task');
          this.showModalGenerateDB = true;
        }
      })
      return data;
    },
    deleteDB() {
      axios.delete('http://localhost:4000/delete_file', { params:{ username: this.$store['state']['user'], path: 'databases/' + this.database_selected + '.json' } }).then(response => {
        if (response.status == 200){
          this.obtainFiles();
          this.database_selected = '';
        }
      })
    },
    clickedOut(event){
      return event;
    },
    // drag drop edits
    isConstant(name){
      return name.startsWith('const:');
    },
    isHidden(name){
      return name == 'empty_';
    },
    isOneParamVar1(idx){
      if (this.configuration_file_opper_apply[idx] != null) return this.configuration_file_opper_apply[idx].num_parameters == 1
    },
    adminImagesVarId(event){
      if (this.configuration_file_var_id.length > this.configuration_file_opper_apply.length){
        this.configuration_file_var_id.splice(event.added.newIndex,1);
        this.showModalErrorDrag = true ;
      }
    },
    generateConstantsDivs(num_parameters){
      if (num_parameters == 2){
        this.configuration_file_var_1.push({name:'const:', id:this.configuration_file_var_1.length});
        this.configuration_file_var_2.push({name:'const:', id:this.configuration_file_var_2.length});
      } else {
        this.configuration_file_var_1.push({name:'const:', id:this.configuration_file_var_1.length});
        this.configuration_file_var_2.push({name:'empty_', id:this.configuration_file_var_2.length});
      }
    },
    adminImagesOpperApply(event){
      if (this.configuration_file_opper_apply.length > this.configuration_file_var_id.length + 1){
        this.showModalErrorDrag = true;
        this.configuration_file_opper_apply.splice(event.added.newIndex,1); 
      }
      if (this.configuration_file_var_1.length < this.configuration_file_opper_apply.length) {
        this.generateConstantsDivs(this.configuration_file_opper_apply[this.configuration_file_opper_apply.length-1].num_parameters);
        return 0;
      }
    },
    adminImagesVar1(event){
      // substituting constants
      if (this.configuration_file_var_1.length > this.configuration_file_var_2.length && this.configuration_file_var_1[event.added.newIndex+1] != null && this.configuration_file_var_1[event.added.newIndex+1].name == 'const:') this.configuration_file_var_1.splice(event.added.newIndex+1, 1)

      if (this.configuration_file_var_1.length > this.configuration_file_opper_apply.length){
        this.configuration_file_var_1.splice(event.added.newIndex,1);
        this.showModalErrorDrag = true ;
      }
    },
    treatMovements(){
      for (var i = 0; i < this.configuration_file_opper_apply.length; i++){
        if (this.configuration_file_opper_apply[i].num_parameters == 1 && this.configuration_file_var_2[i].name != 'empty_') this.configuration_file_var_2.splice(i, 1, {name:'empty_', id:i});
        if (this.configuration_file_opper_apply[i].num_parameters == 2 && this.configuration_file_var_2[i].name == 'empty_') this.configuration_file_var_2.splice(i, 1, {name:'const:', id:i});
      }
    },
    adminImagesVar2(event){
      // substituting constants
      if (this.configuration_file_var_2.length > this.configuration_file_var_1.length && this.configuration_file_var_2[event.added.newIndex+1] != null && this.configuration_file_var_2[event.added.newIndex+1].name == 'const:') this.configuration_file_var_2.splice(event.added.newIndex+1, 1)
      if (this.configuration_file_var_2.length > this.configuration_file_opper_apply.length){
        this.configuration_file_var_2.splice(event.added.newIndex,1);      
        this.showModalErrorDrag = true;
      } 
      this.treatMovements();
    },
    adminImagesDropArea(event) {
      this.adminImagesVarId(event);
      // event for deleting correspondent element if needed
      this.adminImagesOpperApply(event);
      this.adminImagesVar1(event);
      this.adminImagesVar2(event);
      
      this.configuration_file_rows = Math.max(this.configuration_file_var_id.length, this.configuration_file_opper_apply.length,
                                this.configuration_file_var_1.length, this.configuration_file_var_2.length)
    },
    validText(){
      if (this.new_var == '' || !this.new_var.match("^[A-Za-z0-9]+$") || this.new_var.length > 20 || this.edit_var == 'none') return false 
      for(var i=0; i<this.inputs.length; i++) if(this.inputs[i].name == this.new_var) return false;
      return true;
    },
    addNewinput() {
      if (!this.validText()) {
        this.showModalErrorAddVar = true;
      }else {
        this.inputs.push({name: this.new_var, name_tb: this.new_var + '_tb', is_output: false});
        this.showModalAddVar = false;
      }
    },
    changeValuesInPalette(list){
      for(var i=0; i < list.length; i++) if(list[i].name == this.edit_var) list[i].name = this.new_var;
    },
    changeNamesInEdition(index){
      // change values in script
      this.inputs[index].name = this.new_var;
      this.inputs[index].name_tb = this.new_var + '_tb';
      // change values in palette
      this.changeValuesInPalette(this.configuration_file_var_id);
      this.changeValuesInPalette(this.configuration_file_var_1);
      this.changeValuesInPalette(this.configuration_file_var_2);
    },
    editinput() {
      if (!this.validText()) {
        this.showModalErrorAddVar = true;
      }else {
        for (var i=0; i < this.inputs.length; i++) if (this.inputs[i].name == this.edit_var) this.changeNamesInEdition(i);
        this.showModalEditVar = false;
      }
    },
    deleteRow(event){
      if (this.configuration_file_opper_apply.length == 1) return null;   
      var id = event.target.id.substring(event.target.id.lastIndexOf('$')+1)-1;
      // test if delete var id
      if (this.configuration_file_var_id.length == this.configuration_file_opper_apply.length) this.configuration_file_var_id.splice(id, 1);
      // delete other images
      this.configuration_file_opper_apply.splice(id, 1);
      this.configuration_file_var_1.splice(id, 1);
      this.configuration_file_var_2.splice(id, 1);
      
      // update images after deleting row 
      this.configuration_file_rows = Math.max(this.configuration_file_var_id.length, this.configuration_file_opper_apply.length,
                              this.configuration_file_var_1.length, this.configuration_file_var_2.length)
    },
    editConstantVar1(){
      if (this.new_value.match(/^-?[1-9]{1,9}(\.[1-9]{1,9})?$/) != null || this.new_value == ''){
        this.configuration_file_var_1[this.id_click].name = 'const:' + this.new_value;
        this.configuration_file_var_1[this.id_click].abreviation = 'const:' + this.new_value;
        this.showModalEditConstantVar1 = false;
      }
    },
    editConstantVar2(){
      if (this.new_value.match(/^-?[1-9]{1,9}(\.[1-9]{1,9})?$/) || this.new_value == ''){
        this.configuration_file_var_2[this.id_click].name = 'const:' + this.new_value;
        this.configuration_file_var_2[this.id_click].abreviation = 'const:' + this.new_value;
        this.showModalEditConstantVar2 = false;
      }
    },
    deleteVars(event){
      var id = event.target.id.substring(event.target.id.lastIndexOf('$') + 1);
      var list = event.target.id.substring(0, event.target.id.indexOf('$'));
      (list == 'var_1') ? this.configuration_file_var_1.splice(id, 1, {name:'const:', id:id}) : this.configuration_file_var_2.splice(id, 1, {name:'const:', id:id})
    },
    getItemInput(name){
      return { name: name, name_tb: name + '_tb', is_output: false}
    },
    obtainCharacteristicsFromDB(){
      axios.get('http://localhost:4000/list_characteristics', { params:{ username: this.$store['state']['user'], filename: this.database_selected } }).then(response => {
        if (response.status == 200){
          this.original_inputs = [];
          this.inputs = [];
          for (var i = 0; i < response.data['characteristics']['chars'].length; i++){
            this.inputs.push(this.getItemInput(response.data['characteristics']['chars'][i].toLowerCase()));
            this.original_inputs.push(response.data['characteristics']['chars'][i].toLowerCase())
          }
        } else {
          console.log('bad return')
        }
      })
    },
    // Generate configuration JSON
    generateConfigJSON(){
      // Base dict
      var configJSON = {
        name: this.configuration_file_to_generate_name,
        data_operations: {

        },
        output_chars: []
      };
      var var1, var2;
      // Operations
      for(var i = 0; i < this.configuration_file_var_id.length; i++){
        (this.configuration_file_var_1[i].name.startsWith('const:')) ? var1 = Number(this.configuration_file_var_1[i].name.substring(6)) : var1 = this.configuration_file_var_1[i].name;
        (this.configuration_file_var_2[i].name.startsWith('const:')) ? var2 = Number(this.configuration_file_var_2[i].name.substring(6)) : var2 = this.configuration_file_var_2[i].name;
        configJSON['data_operations'][this.configuration_file_var_id[i].name] = [this.configuration_file_opper_apply[i].name, var1, var2];
      }
      // Outputs
      for(i = 0; i < this.inputs.length; i++){
        (this.inputs[i].is_output) ? configJSON['output_chars'].push(this.inputs[i].name) : null;
      }

      console.log(configJSON)

      return configJSON
    },
    // Test integrity of JSON
    testJSON(){
      // testing length
      if (this.configuration_file_var_id.length != this.configuration_file_opper_apply.length) return false;

      // testing conf and existing vars
      for (var i = 0; i < this.configuration_file_var_1.length; i++){
        if (this.configuration_file_var_1[i].name == 'const:' || this.configuration_file_var_2[i].name == 'const:') return false;
      }

      // testing existing vars
      var possibleInputs = this.original_inputs;
      console.log(possibleInputs)
      for (i = 0; i < this.configuration_file_var_1.length; i++){
        if ((!possibleInputs.includes(this.configuration_file_var_1[i].name) && !this.configuration_file_var_1[i].name.startsWith('const:')) || (!possibleInputs.includes(this.configuration_file_var_2[i].name) && !this.configuration_file_var_2[i].name.startsWith('const:'))) return false;
        if (!possibleInputs.includes(this.configuration_file_var_id[i].name)) possibleInputs.push(this.configuration_file_var_id[i].name);
      }

      console.log("passed integrity")

      return true
    },
    // Generate configuration file
    generateConfigFile(){
      if (this.testJSON()){
        var configData = JSON.stringify(this.generateConfigJSON());
        var data = {
          type: 'AddFile',
          username: this.$store['state']['user'],
          filedata: configData,
          path: 'databases_config/' + this.configuration_file_to_generate_name + '.json' 
        }
        console.log(data);
        
        axios.put('http://localhost:4000/', data).then(response => {
          if (response.status == 200){
            console.log('uploaded data, updating view');
            this.showModalGenerateConfigFile = true;
            this.obtainFiles();
          }
        })
        return;
      }
      this.showModalErrorConfigIntegrity = true;
    },
    // Upload DB file
    uploadDB(e){
      e.preventDefault();
      this.$refs.file.file = e.dataTransfer.files[0];
      this.database_files = e.dataTransfer.items;
      if (!this.database_files || this.database_files.length > 1) return;

      if (this.$refs.file.file.name.endsWith('.json')){
        this.showModalUploadDataBase = true;
        var reader = new FileReader();
        reader.readAsText(this.database_files[0].getAsFile());
        reader.onloadend = event => {
          var data = {
            type: 'AddFile',
            username: this.$store['state']['user'],
            filedata: event.target.result,
            path: 'databases/' + this.$refs.file.file.name 
          }
          axios.put('http://localhost:4000/', data).then(response => {
            if (response.status == 200){
              console.log('uploaded data, updating view');

              this.showModalUploadDataBase = false;
              this.obtainFiles();
            }
          })
        }
        
      }
      
    }
  },
  components: {
    Menu,
    Footer,
    draggable,
    VueModal
  },
  props: {
    username: String,
    token: String
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
@import 'data_treatment.css';
</style>

