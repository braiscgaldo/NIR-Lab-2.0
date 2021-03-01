<template>
  <div class="data_treatment-page">
    <div>
      <Menu page="/data_treatment" name="brais" />
    </div>
    <h1>Data Treatment</h1>

    <!-- Selects and geners -->
    <div id="gen_sel">
      <!-- Generate new database div -->
      <div id="gen_new_db">
        <form class="vue-form gen_db_form" @submit.prevent="generateNewDB">
          <fieldset>
            <label>Generate a database!</label>
            <input
              type="text"
              placeholder="Database name"
              name="database_to_generate_name"
              id="database_to_generate_name"
              required
              v-model="database_to_generate_name"
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
            >
            </v-combobox>
            <div class="drop-zone">
              <b-img
                class="db_image"
                :src="getImgDb()"
                alt="Database image"
              ></b-img>
            </div>
          </fieldset>
        </form>
      </div>
    </div>

    <h2>Design Palette</h2>
    <!-- Desing Palette -->
    <div id="design_palette">
      <div id="palette">
        <form
          class="vue-form palette_form"
          @submit.prevent="generateNewConfigFile"
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
        <div class="border_rect">
          <div id="var_id">
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
                <div :id="'var_id$' + Input.name + '$' + idx" class="input_drop" @dblclick="deleteDiv">
                  {{ Input.name }}
                </div>
              </div>
            </draggable>
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
                <div :id="'opper_apply$' + operation.name + '$' + idx" class="operation_drop" @dblclick="deleteDiv">
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
              :move="adminImagesDropArea"
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
                <div v-else-if="isOneParamVar1(idx)" :id="'var_1$' + Input.name + '$' + idx" class="input_drop var_1_one_param" @dblclick="deleteDiv">
                  {{ Input.name }}
                </div>
                <div v-else :id="'var_1$' + Input.name + '$' + idx" class="input_drop" @dblclick="deleteDiv">
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
              :move="adminImagesDropArea"
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
                <div v-else-if="isHidden(Input.name)" :id="'var_2$' + Input.name + '$' + idx" class="input_drop" style="visibility:hidden" @dblclick="deleteDiv">
                  {{ Input.name }}
                </div>
                <div v-else :id="'var_2$' + Input.name + '$' + idx" class="input_drop" @dblclick="deleteDiv">
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
                  rotation="90"
                ></b-img>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div id="inputs">
        <h3>Operations</h3>
        <h3 id="var_title">inputs</h3>
        <h3 id="output_title">Output</h3>

        <div class="border_rect" id="var_names">
          <draggable
              :list="inputs"
              ghost-class="ghost"
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
                
              
                <div :id="Input.name" class="Input Input-inside" @dblclick="showModalEditVar=true; new_var=Input.name; edit_var=new_var">
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
            :list="operations"
            class="list-group"
            ghost-class="ghost"
            :move="adminImagesDropArea"
            :group="{ name: 'operations', pull: 'clone', put: false }"
          >
            <div
              class="container"
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
</template>

<script>
import Menu from "../../common/header/private/menu.vue";
import Footer from "../../common/footer/footer.vue";
import draggable from "vuedraggable";
// dialogs
import VueModal from '@kouts/vue-modal';


export default {
  data: () => ({
    // for generating new db parting from files
    database_to_generate_name: "",
    configuration_file_selected: "",
    configuration_files: ["config_file_1", "config_file_2", "config_file_3"],
    // for delete // generate databases
    database_selected: "",
    databases_names: ["HadaBeer", "Chocolate", "Tejidos"],
    // for generate config -- DESINGN PALETTE
    configuration_file_to_generate_name: "",
    showModalAddVar: false,
    showModalErrorAddVar: false,
    showModalEditVar: false,
    showModalErrorDrag: false,
    showModalEditConstantVar1: false,
    showModalEditConstantVar2: false,
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
    configuration_file_var_id: [
      {
        name: "intensity",
        id: 0
      }
    ],
    configuration_file_opper_apply: [
      {
        name: "sum",
        abreviation: 'sum',
        id: 0
      }
    ],
    configuration_file_var_1: [
      {
        name: "reflectance",
        id: 0
      }
    ],
    configuration_file_var_2: [
      {
        name: "absorbance",
        id: 0
      }
    ],
    configuration_file_rows: 1
  }),
  methods: {
    getImgDb() {
      return require("/src/assets/private/data_treatment/data_base.png");
    },
    getImgArrow() {
      return require("/src/assets/private/data_treatment/arrow_right.png");
    },
    getImgKey() {
      return require("/src/assets/private/data_treatment/key_right.png");
    },
    generateNewDB() {
      // Call Facade
      return null;
    },
    deleteDB() {
      // Call Facade
      return null;
    },
    clickedOut(event){
      return event;
    },
    isConstant(name){
      return name.startsWith('const:');
    },
    isHidden(name){
      return name == 'empty_';
    },
    isOneParamVar1(idx){
      return this.getOpperItem(this.configuration_file_opper_apply[idx].name).num_parameters == 1
    },
    getOpperItem(name){
      for(var i = 0; i < this.operations.length; i++){
        if (this.operations[i].name == name) return this.operations[i]
      }
    },
    updateIdDropArea(list, i){
      var id = list[i].name + '$' + list[i].id;
      list[i].id = i
      document.getElementById(id).id = list[i].name + '$' + i  
    },
    adminImagesVarId(){
      for (var i = 0; i < this.configuration_file_var_id.length; i++){
        this.configuration_file_var_id[i].id = i;
        var id = 'var_id$' + this.configuration_file_var_id[i].name + '$' + this.configuration_file_var_id[i].id;
        document.getElementById(id).id = 'var_id$' + this.configuration_file_var_id[i].name + '$' + i 
        this.configuration_file_var_id[i].id = i;
      }
    },
    generateConstantsDivs(name_op){
      if (this.getOpperItem(name_op).num_parameters == 2){
        this.configuration_file_var_1.push({name:'const:', id:this.configuration_file_var_1.length});
        this.configuration_file_var_2.push({name:'const:', id:this.configuration_file_var_2.length});
      } else {
        this.configuration_file_var_1.push({name:'const:', id:this.configuration_file_var_1.length});
        this.configuration_file_var_2.push({name:'empty_', id:this.configuration_file_var_2.length});
      }
      console.log(this.configuration_file_var_1)
    },
    adminImagesOpperApply(){
      for (var i = 0; i < this.configuration_file_opper_apply.length; i++){
        if (this.configuration_file_opper_apply.length > this.configuration_file_var_id.length){
          this.showModalErrorDrag = true;
          this.configuration_file_opper_apply.splice(-1,1);
        }else{
          this.configuration_file_opper_apply[i].id = i;
          var id = 'opper_apply$' + this.configuration_file_opper_apply[i].name + '$' + this.configuration_file_opper_apply[i].id;
          document.getElementById(id).id = 'opper_apply$' + this.configuration_file_opper_apply[i].name + '$' + i 
          this.configuration_file_opper_apply[i].id = i;
          if (this.configuration_file_var_1.length < this.configuration_file_opper_apply.length) this.generateConstantsDivs(this.configuration_file_opper_apply[this.configuration_file_opper_apply.length-1].name)
        }
      }
    },
    adminImagesVar1(){
      for (var i = 0; i < this.configuration_file_var_1.length; i++){
        if (this.configuration_file_var_1.length > this.configuration_file_opper_apply.length){
          this.showModalErrorDrag = true;
          this.configuration_file_var_1.splice(-1,1);
        } else {
          this.configuration_file_var_1[i].id = i
          var id = 'var_1$' + this.configuration_file_var_1[i].name + '$' + this.configuration_file_var_1[i].id;
          if (document.getElementById(id) != null) document.getElementById(id).id = 'var_1$' + this.configuration_file_var_1[i].name + '$' + i 
          this.configuration_file_var_1[i].id = i;
        }
      }
    },
    treatMovements(){
      for (var i = 0; i < this.configuration_file_opper_apply.length; i++){
        console.log(this.configuration_file_opper_apply[i])
        if (this.getOpperItem(this.configuration_file_opper_apply[i].name).num_parameters == 1 && this.configuration_file_var_2[i].name != 'empty_') this.configuration_file_var_2.splice(i, 1, {name:'empty_', id:i});
        if (this.getOpperItem(this.configuration_file_opper_apply[i].name).num_parameters == 2 && this.configuration_file_var_2[i].name == 'empty_') this.configuration_file_var_2.splice(i, 1, {name:'const:', id:i});
      }
    },
    adminImagesVar2(){
      for (var i = 0; i < this.configuration_file_var_2.length; i++){
        if (this.configuration_file_var_2.length > this.configuration_file_var_1.length){
          this.showModalErrorDrag = true;
          this.configuration_file_var_2.splice(-1,1);
        } else {
          this.configuration_file_var_2[i].id = i
          var id = 'var_2$' + this.configuration_file_var_2[i].name + '$' + this.configuration_file_var_2[i].id;
          if (document.getElementById(id) != null) document.getElementById(id).id = 'var_2$' + this.configuration_file_var_2[i].name + '$' + i 
          this.configuration_file_var_2[i].id = i;
        }
        this.treatMovements();
      }
    },
    adminImagesDropArea() {
      this.configuration_file_rows = Math.max(this.configuration_file_var_id.length, this.configuration_file_opper_apply.length,
                                this.configuration_file_var_1.length, this.configuration_file_var_2.length)
      this.adminImagesVarId()
      this.adminImagesOpperApply();
      this.adminImagesVar1();
      this.adminImagesVar2();
    },
    validText(){
      if (this.new_var == '' || !this.new_var.match("^[A-Za-z0-9]+$") || this.new_var.length > 20 || this.edit_var == 'none') return false 
      for(var i=0; i<this.inputs.length; i++){
        if(this.inputs[i].name == this.new_var) return false;
      }
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
    changeValuesInPalette(prefix, list){
      for(var i=0; i < list.length; i++){
        if(list[i].name == this.edit_var){
          document.getElementById(prefix + list[i].name + '$' + list[i].id).innerHTML = this.new_var;
          document.getElementById(prefix + list[i].name + '$' + list[i].id).id = prefix + this.new_var + '$' + list[i].id;
          list[i].name = this.new_var;
        }
      }
    },
    changeNamesInEdition(index){
      // change values in script
      this.inputs[index].name = this.new_var;
      this.inputs[index].name_tb = this.new_var + '_tb';
      // change values in html
      document.getElementById(this.edit_var).innerHTML = this.new_var;
      document.getElementById(this.edit_var).id = this.new_var;
      // change values in palette
      this.changeValuesInPalette('var_id$', this.configuration_file_var_id);
      this.changeValuesInPalette('var_1$', this.configuration_file_var_1);
      this.changeValuesInPalette('var_2$', this.configuration_file_var_2);
    },
    editinput() {
      if (!this.validText()) {
        this.showModalErrorAddVar = true;
      }else {
        for (var i=0; i < this.inputs.length; i++){
          if (this.inputs[i].name == this.edit_var) this.changeNamesInEdition(i);
        }
        this.showModalEditVar = false;
      }
    },
    deleteDiv(event){
      if (this.configuration_file_var_id.length == 1 || this.configuration_file_var_1.length == 1 || this.configuration_file_var_2 == 1) return null;   
      
      var id = event.target.id.substring(event.target.id.lastIndexOf('$') + 1);
      var list = event.target.id.substring(0, event.target.id.indexOf('$'));
      console.log('deleting ' + id, list)
      if (list == 'var_id' && this.configuration_file_var_id.length > this.configuration_file_opper_apply.length){
        this.configuration_file_var_id.splice(id, 1);
      }else if (list == 'opper_apply'){
        this.configuration_file_opper_apply.splice(id, 1);
        this.configuration_file_var_1.splice(id, 1);
        this.configuration_file_var_2.splice(id, 1);
      }
    },
    editConstantVar1(){
      if (parseFloat(this.new_value) != null){
        for (var i = 0; i < this.configuration_file_var_1.length; i++){
          if (this.configuration_file_var_1[i].id == this.id_click) {
            this.configuration_file_var_1[i].name = 'const:' + this.new_value;
            this.showModalEditConstantVar1 = false;
            break;
          }
        }
      }
    },
    editConstantVar2(){
      if (parseFloat(this.new_value) != null){
        for (var i = 0; i < this.configuration_file_var_2.length; i++){
          if (this.configuration_file_var_2[i].id == this.id_click) {
            this.configuration_file_var_2[i].name = 'const:' + this.new_value;
            this.showModalEditConstantVar2 = false;
            break;
          }
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
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.data_treatment-page {
  background-color: #deeaee;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

#gen_sel {
  margin-top: 1vw;
  display: flex;
  padding: 0vw;
}

#gen_new_db {
  float: left;
  width: 50%;
}

.gen_db_form,
.sel_db_form {
  margin: 2vw;
  width: 90%;
  border-radius: 0.75vw;
}

#select_db {
  float: right;
  width: 50%;
  height: 100%;
}

.vue-form div {
  margin: 2px 0 !important;
}

h1 {
  margin-top: 4vw;
  margin-bottom: 3vw;
  text-align: center;
}

h2 {
  margin-top: 5vw;
  text-align: center;
}

.delete_button {
  background-color: red !important;
  margin-top: -1.5vw;
}

.border_rect {
  width: fit-content;
}

#design_palete {
  margin-top: auto !important;
  max-height: fit-content;
}

.drop-zone {
  border: solid 1px #ee3744;
  padding: 10px;
  border-radius: 0.75vw;
  height: inherit;
  text-align: center;
}

.db_image {
  width: 10%;
}

/* Design palette */
#palette {
  float: left;
  margin: 0 2vw;
  width: 50%;
  max-height: fit-content;
}

.palette_form {
  width: 100%;
  border-radius: 0.75vw;
}

.cf_button {
  width: 35%;
  border: 0vw;
  font-size: 10pt;
}

#configuration_file_to_generate_name {
  width: 60%;
  padding: 0.55vw;
}

h3 {
  padding-top: 5vw;
  float: right;
  margin-right: 3.5vw;
}

#var_title {
  margin-right: 13.4vw;
}

#output_title {
  margin-right: 6.6vw;
}

#inputs {
  float: right;
  width: 45%;
  padding: 0%;
  max-width: fit-content;
}

#var_names {
  float: left;
  width: 66%;
  margin-top: 4vw;
  text-align: center;
}

#operation_names {
  float: right;
  width: 34%;
  margin-right: 1.5vw;
  margin-top: 4vw;
  max-width: fit-content;
}

/*******************  Records ****************************/

.container {
  max-width: fit-content;
  padding: 0%;
  position: relative;
  height: auto;
  display: block;
  width: 100% !important;
}

/*********************** inputs **********************************/

.container_var {
  align-items: baseline;
  display: inline-flex;
  float: right;
  margin-left: 50%;
}

#var_container {
  padding: 0;
  vertical-align: middle;
}

.Input {
  padding: 5px;
  margin: 5px;
  margin-left: 0%;
  margin-right: 0%;
  background-color: #ee3744;
  border: solid 1px #ee3744;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
  width: 73%;
  float: right;
}

.Input-inside {
  margin-left: 0%;
  margin-right: 0%;
  background-color: #ee3744;
  border: solid 1px #ee3744;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
  width: 100%;
  float: right;
}

.toggle_button {
  margin: 5px;
  margin-right: 5%;
  float: left;
  width: fit-content;
  align-self: center;
}

.add_input_button {
  background-color: #ee3744;
  border: solid 1px #ee3744;
  color: #deeaee;
  border-radius: 1em;
  font-size: 1.5vw;
  text-align: center;
  width: 2.5vw;

}


/******************** Operations *******************************/

.operation {
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


/************** Drop Zone ******************/
.input_drop {
  padding: 5px;
  margin: 5px;
  background-color: #ee3744;
  border: solid 1px #ee3744;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
}

.operation_drop {
  padding: 5px;
  margin: 5px;
  background-color: #2fa2a2;
  border: solid 1px #2fa2a2;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
}

#var_id, #oper_apply, #var_1, #var_2 {
  width: 33%;
  display: inline-flex;
}

#var_1 {
  margin-right: 3vw;
}

.arrow_img {
  width: 4.5vw;
  margin-left: 1vw;
  margin-right: 1vw;
  transform: rotate(180deg);
}

.key_img {
  margin: 0px;
  width: 1.35vw;
  margin-bottom: 0.18vw;
}

.key_left_img {
  width: 1.35vw; 
  margin: 0px;
  margin-bottom: 0.18vw;
  transform: rotate(180deg);
}

.images_drop {
  position: relative;
}

.add_input_input {
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

.add_input_dialog_button{
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

/***************** Constants ******************/
.constant {
  padding: 5px;
  margin: 5px;
  background-color: #31962f;
  border: solid 1px #31962f;
  color: #deeaee;
  border-radius: 0.75em;
  font-size: 1.5vw;
  text-align: center;
}

.var_1_one_param {
  padding-inline-start: 100%;
  padding-inline-end: 100%;
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

</style>

