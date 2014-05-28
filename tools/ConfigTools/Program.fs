// Learn more about F# at http://fsharp.net
// See the 'F# Tutorial' project for more help.
namespace ConfigTools

open InputFormsTool
open System.IO


module Main = 

[<EntryPoint>]
let main argv = 
    
    //let inputForms = load None
    let inputForms = load (Some "../../../../dspace/config/input-forms-template.xml")
    let genericForm = inputForms |> form "{itemType}"

    
    let page1Fields = 
        genericForm 
        |> pageFields 1 None
    
    let page2Fields = 
        genericForm 
        |> pageFields 2 (Some "{itemType}")
   
    let writeField (fld: InputFormsConfig.Field) = printfn "%s.%s" fld.DcSchema fld.DcElement
    let writeFields (fields: InputFormsConfig.Field seq) =
        fields 
        |> Seq.iter writeField
    
    let fieldFilter lng itemTypeName (fld: InputFormsConfig.Field) = 
        let inputType (inpTyp:InputFormsConfig.InputType) = 
            let getValPairsName (v:string option) = 
                match v with 
                | Some(x) -> Some(x.Replace("{itemType}", "-" + itemTypeName))
                | _ -> None
            InputFormsConfig.InputType((getValPairsName inpTyp.ValuePairsName), inpTyp.Value)

        match lng with
        | l when l = "el" -> InputFormsConfig.Field(fld.DcSchema, fld.DcElement, fld.DcQualifier, fld.Repeatable, fld.LabelEl.Value, None, (inputType fld.InputType), fld.HintEl, None, fld.RequiredEl, None, fld.I18n)
        | _ -> InputFormsConfig.Field(fld.DcSchema, fld.DcElement, fld.DcQualifier, fld.Repeatable, fld.Label, None, (inputType fld.InputType), fld.Hint, None, fld.Required, None, fld.I18n)
            

    //let lang = "el"
    //let itemTypeName = "bachelorThesis"

    let createFields lang itemTypeName =         
        Seq.map (fun fn -> field fn page2Fields |> fieldFilter lang itemTypeName)

    let createPage num typeName flds = 
        let fieldsArray = flds |> Seq.toArray
        InputFormsConfig.Page(num, typeName, fieldsArray)    

    //let p1Fields = createFields ["type";"title"]
    let p1Fields lang itemTypeName = page1Fields |> Seq.map (fun f -> fieldFilter lang itemTypeName f)
    let createPage1 = createPage 1 None
    
    let page1 lang itemTypeName = itemTypeName |> p1Fields lang |> createPage1
    

    let createForm lang itemTypeName page2FieldNames = 
        let p2Fields = createFields lang itemTypeName page2FieldNames
        let page2 = createPage 2 (Some itemTypeName) p2Fields;
        InputFormsConfig.Form(itemTypeName, [| itemTypeName |> page1 lang ; page2 |])
                
    
    let itemTypes = ["bachelorThesis";"masterThesis";"doctoralThesis"]    
    
    let generateForms lang fldNames =
        Seq.map (fun i -> createForm lang i fldNames)

    let fieldNames = ["publicationDate";"abstract";"sponsor";"advisorName";"committeeMemberName";"academicPublisherID";"academicPublisher";"numberOfPages";"generalDescription"]


    let forms lang = itemTypes |> generateForms lang fieldNames

    let writeFile lang = 
        use wr = new StreamWriter("../../../../dspace/config/input-forms_" + lang + ".temp.xml", false)
        let f = forms lang |> Seq.toArray
        let formDef = InputFormsConfig.FormDefinitions(f)        
        wr.Write(formDef);
        wr.Close();
    
    writeFile "en"
    writeFile "el"
    
//    let forms
//        createForm "en" "bachelorThesis" ["publicationDate";"abstract";"sponsor";"advisorName";"committeeMemberName";"academicPublisherID";"academicPublisher";"numberOfPages";"generalDescription"]::    
//        createForm "en" "masterThesis" ["publicationDate";"abstract";"sponsor";"advisorName";"committeeMemberName";"academicPublisherID";"academicPublisher";"numberOfPages";"generalDescription"]::
//        []
//    f1 |> printfn "%A"

    //["type";"title"] |> Seq.map (fun fn -> field fn page1Fields) |> Seq.iter (fun f -> printfn "%A" f)

//    let healType = page1Fields |> field "type" 
//    healType |> printfn "%A"
    //page1Fields |> writeFields
    //page2Fields |> writeFields
    
    
    

    System.Console.ReadLine() |> ignore
    0 // return an integer exit code
