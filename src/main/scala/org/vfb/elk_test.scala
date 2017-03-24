package main.scala.org.vfb
import org.semanticweb.elk.owlapi.ElkReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.reasoner.InferenceType
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.util.SimpleShortFormProvider
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter
import org.semanticweb.owlapi.search.EntitySearcher
import org.semanticweb.owlapi.model.OWLClass
// See Brain for how to use the following to roll class expressions from MS:
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxClassExpressionParser
import scala.collection.JavaConversions._
import java.io.File
import java.util.Set

// Playing around with OWL API v4 & ELK in Scala
// see https://github.com/balhoff/test-elk/blob/master/src/main/scala/org/nescent/Main.scala
// see also https://github.com/VirtualFlyBrain/VFB_owl/blob/master/src/code/mod/owl2pdm_tools.py 
// for similar code in Jython using OWL-API v3
// see https://github.com/owlcs/owlapi/wiki/Migrate-from-version-3.4-and-3.5-to-4.0
// For migration of methods from 3-4

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
    val simple_sfp = new SimpleShortFormProvider
    private val onts = collection.mutable.Set(ontology) 
    // scala.collection.JavaConversions._ magic takes care of casting onts 
    // to appropriate java type for the following constructor:
    val bi_sfp = new BidirectionalShortFormProviderAdapter(manager, onts, simple_sfp)
 
  
  
  def getSubClasses(short_form: String): Set[OWLClass]  = {
    val e = bi_sfp.getEntity(short_form)
    val c = e.asOWLClass
    return this.reasoner.getSubClasses(c, false).getFlattened
  }
    
  def getAnnotations(short_form: String) {
    val e = bi_sfp.getEntity(short_form)
    val annotations =  EntitySearcher.getAnnotations(e, ontology)
  //(see https://github.com/owlcs/owlapi/wiki/Migrate-from-version-3.4-and-3.5-to-4.0)
    for (a <- annotations) {
      println(a.toString())
    }
    println(annotations.toString())
  }
  
  /// Example of getting entiry from iri.  
  /// ## TODO turn this into alternative method
  val iri = IRI.create("http://purl.obolibrary.org/obo/FBbt_00003649")
  
  val rNeuron = factory.getOWLClass(iri)
  

 //get asserted superclasses:
  
  val rNeuron_rel = EntitySearcher.getSuperClasses(rNeuron, ontology) 
  //( only works with class object - rather than any old OWL entity.)
  
  for (r <- rNeuron_rel) {
    println(r.toString())
  } 


}
