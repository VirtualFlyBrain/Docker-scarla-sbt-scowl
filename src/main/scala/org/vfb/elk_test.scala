package main.scala.org.vfb
import org.semanticweb.elk.owlapi.ElkReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.reasoner.InferenceType
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.util.SimpleShortFormProvider
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter
import org.semanticweb.owlapi.search.EntitySearcher
import org.semanticweb.owlapi.model.OWLClass
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxClassExpressionParser
import scala.collection.JavaConversions._
import java.io.File
import java.util.Set

// see https://github.com/balhoff/test-elk/blob/master/src/main/scala/org/nescent/Main.scala
// see also https://github.com/VirtualFlyBrain/VFB_owl/blob/master/src/code/mod/owl2pdm_tools.py 
// for similar code in Jython using OWL-API v3

case class elk_test(var iri_string: String) {
 
  //TODO Turn this into a wrapper class for factory, manager, ontology etc
  //Add some simple methods to return strings, map object etc as in owl2pdm_tools.py
  // and Brain
  
  val factory = OWLManager.getOWLDataFactory
  val manager = OWLManager.createOWLOntologyManager
//  val ontology = manager.loadOntologyFromOntologyDocument(new File("/repos/drosophila-anatomy-developmental-ontology/fbbt/releases/fbbt-simple.owl"))
  val ont_iri = IRI.create(iri_string)
  val ontology = manager.loadOntology(ont_iri)
  
  val reasoner = new ElkReasonerFactory().createReasoner(ontology)
  
  reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY)
  
  
  val iri = IRI.create("http://purl.obolibrary.org/obo/FBbt_00003649")
  val rNeuron = factory.getOWLClass(iri)
  
  val rNeueonr_sub = reasoner.getSubClasses(rNeuron, false)
  
  val rNeuron_super = reasoner.getSuperClasses(rNeuron, false)
  

  
   //  Accessing the same class using short_form
   
  val simple_sfp = new SimpleShortFormProvider
  val onts = collection.mutable.Set(ontology) 
  // scala.collection.JavaConversions._ magic takes care of casting onts 
  // to appropriate java type for the following constructor:
  val bi_sfp = new BidirectionalShortFormProviderAdapter(manager, onts, simple_sfp)
  val e = bi_sfp.getEntity("FBbt_00003649")
 

  def getSubClasses(short_form: String): Set[OWLClass]  = {
    val e = bi_sfp.getEntity(short_form)
    val c = e.asOWLClass
    return this.reasoner.getSubClasses(c, false).getFlattened
  }
  
  // How to get annotations? There is no longet a method 'getAnnotations' 
  // on OWLEntity in v4. Instead use:

  val rNeuron_annotations = EntitySearcher.getAnnotations(e, ontology)
  //(see https://github.com/owlcs/owlapi/wiki/Migrate-from-version-3.4-and-3.5-to-4.0)  
  println(rNeuron_annotations.toString())
  
  for (a <- rNeuron_annotations) {
    println(a.toString())
  }
  
  //get asserted superclasses:
  
  val rNeuron_rel = EntitySearcher.getSuperClasses(rNeuron, ontology) 
  //( only works with class object - rather than any old OWL entity.)
  
  for (r <- rNeuron_rel) {
    println(r.toString())
  }
  
  //similarly, for an ind we can use EntitySearcher.getTypes etc

}
