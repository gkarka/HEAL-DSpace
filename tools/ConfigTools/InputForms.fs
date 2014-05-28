namespace ConfigTools

open FSharp.Data

module InputFormsTool =

    type InputFormsConfig = XmlProvider<"../../dspace/config/input-forms-template.xml">
               
    let form name (cfg:InputFormsConfig.InputForms) = query {
        for f in cfg.FormDefinitions.Forms do
        where (f.Name = name)
        select f
        exactlyOneOrDefault
        }    
    
    let pageFields pageNum (typeName:string option) (frm:InputFormsConfig.Form) = query { 
        for p in frm.Pages do
        where (p.Number = pageNum)
        where (p.TypeName = typeName)
        select p.Fields
        exactlyOneOrDefault
    }    
    
    
    let field name (fields:InputFormsConfig.Field seq) = query {
        for f in fields do
        where (f.DcElement = name)
        select f
        exactlyOneOrDefault
    }

    let load file = 
        match file with        
        | (Some(f:string)) -> InputFormsConfig.Load(f)
        | (None) -> InputFormsConfig.GetSample()    
        
    