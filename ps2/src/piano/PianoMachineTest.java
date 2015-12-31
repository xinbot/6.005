package piano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.sound.midi.MidiUnavailableException;

import midi.Instrument;
import midi.Midi;
import music.NoteEvent;
import music.Pitch;

import org.junit.Test;

public class PianoMachineTest {
	
	PianoMachine pm = new PianoMachine();
	
    @Test
    public void singleNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	
        pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        assertEquals(expected0,midi.history());
    }
    
    @Test
    public void multiNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(500) off(61,PIANO) wait(0) on(62,PIANO) "
        				 + "wait(100) off(62,PIANO) wait(0) on(65,PIANO) wait(80) off(65,PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	
        pm.beginNote(new Pitch(1));
        Midi.wait(500);
        pm.endNote(new Pitch(1));
        Midi.wait(0);
        pm.beginNote(new Pitch(2));
		Midi.wait(100);
		pm.endNote(new Pitch(2));
		Midi.wait(0);
		pm.beginNote(new Pitch(5));
		Midi.wait(80);
		pm.endNote(new Pitch(5));
		
        assertEquals(expected0,midi.history());
        
        
       String expected1 = "on(60,PIANO) wait(3) on(65,PIANO) wait(10) off(60,PIANO) wait(3) off(65,PIANO)";
       
       midi.clearHistory();
       pm.beginNote(new Pitch(0));
       Midi.wait(3);
       pm.beginNote(new Pitch(5));
       Midi.wait(10);
       pm.endNote(new Pitch(0));
       Midi.wait(3);
       pm.endNote(new Pitch(5));
       
       assertEquals(expected1,midi.history());
    }
    
    @Test
    public void changeInstrumentTest() throws MidiUnavailableException{
    	String expected = "on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	pm.changeInstrument();
    	pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        assertEquals(expected,midi.history());
        
        
       // HONKY_TONK_PIANO
        midi.clearHistory();
        pm.changeInstrument();
        pm.changeInstrument();
        pm.beginNote(new Pitch(2));
        Midi.wait(30);
        pm.beginNote(new Pitch(3));
		Midi.wait(100);
		pm.endNote(new Pitch(2));
		Midi.wait(10);
		pm.endNote(new Pitch(3));
		
		expected = "on(62,HONKY_TONK_PIANO) wait(30) on(63,HONKY_TONK_PIANO) "
				 + "wait(100) off(62,HONKY_TONK_PIANO) wait(10) off(63,HONKY_TONK_PIANO)";
		
		assertEquals(expected,midi.history());
		
		//MUSIC_BOX
		midi.clearHistory();
		pm.changeInstrument();
        pm.changeInstrument();
        pm.changeInstrument();
        pm.changeInstrument();
        pm.changeInstrument();
        pm.changeInstrument();
        pm.changeInstrument();
        
        pm.beginNote(new Pitch(8));
		Midi.wait(100);
		pm.endNote(new Pitch(8));
		Midi.wait(10);
		pm.beginNote(new Pitch(1));
		Midi.wait(0);
		pm.endNote(new Pitch(1));
		
		expected = "on(68,MUSIC_BOX) wait(100) off(68,MUSIC_BOX) "
				 + "wait(10) on(61,MUSIC_BOX) wait(0) off(61,MUSIC_BOX)";
		
		assertEquals(expected,midi.history());
		
    }
    
    @Test
    public void shiftUpTest() throws MidiUnavailableException {
    	String expected = "on(73,PIANO) wait(100) off(73,PIANO)";
        
    	Midi midi = Midi.getInstance();
    	
    	pm.shiftUp();
    	
    	midi.clearHistory();
    	pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        assertEquals(expected,midi.history());
        
        expected = "on(85,PIANO) wait(100) off(85,PIANO)";
        
        pm.shiftUp();
        pm.shiftUp();
        
        midi.clearHistory();
    	pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));
		
		assertEquals(expected,midi.history());
    }
    
    @Test
    public void shiftDownTest() throws MidiUnavailableException {
    	String expected = "on(49,PIANO) wait(100) off(49,PIANO)";
        
    	Midi midi = Midi.getInstance();
    	
    	pm.shiftDown();
    	
    	midi.clearHistory();
    	pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        assertEquals(expected,midi.history());
        
        pm.shiftDown();
        
        expected = "on(36,PIANO) wait(100) off(36,PIANO)";
        
        midi.clearHistory();
    	pm.beginNote(new Pitch(0));
		Midi.wait(100);
		pm.endNote(new Pitch(0));

        assertEquals(expected,midi.history());
        
        pm.shiftDown();
        pm.shiftDown();
        pm.shiftDown();
        pm.shiftDown();
        
        expected = "on(40,PIANO) wait(100) off(40,PIANO)";
        
        midi.clearHistory();
    	pm.beginNote(new Pitch(4));
		Midi.wait(100);
		pm.endNote(new Pitch(4));

        assertEquals(expected,midi.history());
    }
    
    @Test
    public void recordTest() throws MidiUnavailableException {
    	
    	PianoMachine pm = new PianoMachine();
    	
    	Midi midi = Midi.getInstance();
    	midi.clearHistory();
    	
    	assertFalse(pm.isRecording);
    	
    	pm.toggleRecording();
    	assertTrue(pm.isRecording);
    	pm.toggleRecording();
    	assertFalse(pm.isRecording);
    	
    	pm.beginNote(new Pitch(0));
		Midi.wait(100);
		pm.endNote(new Pitch(0));
		
		assertEquals(pm.recordedEvents, new ArrayList<NoteEvent>());
		
		
		Pitch expectedPitch = new Pitch(0);
    	Instrument expectedInstrument = Instrument.PIANO;
    	NoteEvent.Kind expectedBeginNote = NoteEvent.Kind.start;
    	NoteEvent.Kind expectedEndNote = NoteEvent.Kind.stop;

    	midi.clearHistory();
    	
    	assertFalse(pm.isRecording);
    	
    	pm.toggleRecording();
    	assertTrue(pm.isRecording);
    	
    	pm.beginNote(new Pitch(0));
		Midi.wait(100);
		pm.endNote(new Pitch(0));
		
		pm.toggleRecording();
    	assertFalse(pm.isRecording);
		
    	assertEquals(pm.recordedEvents.get(0).getPitch(), expectedPitch);
		assertEquals(pm.recordedEvents.get(0).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(0).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(1).getPitch(), expectedPitch);
		assertEquals(pm.recordedEvents.get(1).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(1).getKind(), expectedEndNote);
		
		
		expectedPitch = new Pitch(0);
    	expectedInstrument = Instrument.PIANO;
    	expectedBeginNote = NoteEvent.Kind.start;
    	expectedEndNote = NoteEvent.Kind.stop;
    	
    	midi.clearHistory();
    	
    	assertFalse(pm.isRecording);
    	
    	pm.toggleRecording();
    	assertTrue(pm.isRecording);
    	
    	pm.beginNote(new Pitch(0));
		Midi.wait(100);
		pm.endNote(new Pitch(0));
		
		pm.toggleRecording();
    	assertFalse(pm.isRecording);
		
    	assertEquals(pm.recordedEvents.get(0).getPitch(), expectedPitch);
		assertEquals(pm.recordedEvents.get(0).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(0).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(1).getPitch(), expectedPitch);
		assertEquals(pm.recordedEvents.get(1).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(1).getKind(), expectedEndNote);
    }
    
    @Test
    public void overrideRecordTest() throws MidiUnavailableException {
    	PianoMachine pm = new PianoMachine();
    	
    	Pitch expectedPitch0 = new Pitch(0);
    	Pitch expectedPitch2 = new Pitch(2);
    	Pitch expectedPitch5 = new Pitch(5);
    	Pitch expectedPitch7 = new Pitch(7);
    	Pitch expectedPitch10 = new Pitch(10);
    	Instrument expectedInstrument = Instrument.PIANO;
    	NoteEvent.Kind expectedBeginNote = NoteEvent.Kind.start;
    	NoteEvent.Kind expectedEndNote = NoteEvent.Kind.stop;
    	
    	Midi midi = Midi.getInstance();
    	midi.clearHistory();
    	
    	assertFalse(pm.isRecording);
    	
    	// the initial recording
    	pm.toggleRecording();
    	assertTrue(pm.isRecording);
    	
    	// the first note event
    	pm.beginNote(new Pitch(0));
		Midi.wait(100);
		pm.endNote(new Pitch(0));
		
		// the second note event
		Midi.wait(10);
		pm.beginNote(new Pitch(5));
		Midi.wait(100);
		pm.endNote(new Pitch(5));
		
		pm.toggleRecording();
    	assertFalse(pm.isRecording);
		
    	assertEquals(pm.recordedEvents.get(0).getPitch(), expectedPitch0);
		assertEquals(pm.recordedEvents.get(0).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(0).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(1).getPitch(), expectedPitch0);
		assertEquals(pm.recordedEvents.get(1).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(1).getKind(), expectedEndNote);
		
		assertEquals(pm.recordedEvents.get(2).getPitch(), expectedPitch5);
		assertEquals(pm.recordedEvents.get(2).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(2).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(3).getPitch(), expectedPitch5);
		assertEquals(pm.recordedEvents.get(3).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(3).getKind(), expectedEndNote);
		
		// the new recording, expected to override the previous one
    	pm.toggleRecording();
    	assertTrue(pm.isRecording);
    	
    	// the first note event
    	pm.beginNote(new Pitch(2));
		Midi.wait(100);
		pm.endNote(new Pitch(2));
		
		// the second note event
		Midi.wait(10);
		pm.beginNote(new Pitch(7));
		Midi.wait(100);
		pm.endNote(new Pitch(7));
		
		// the third note event
		Midi.wait(10);
		pm.beginNote(new Pitch(10));
		Midi.wait(100);
		pm.endNote(new Pitch(10));
		
		pm.toggleRecording();
    	assertFalse(pm.isRecording);
		
    	assertEquals(pm.recordedEvents.get(0).getPitch(), expectedPitch2);
		assertEquals(pm.recordedEvents.get(0).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(0).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(1).getPitch(), expectedPitch2);
		assertEquals(pm.recordedEvents.get(1).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(1).getKind(), expectedEndNote);
		
		assertEquals(pm.recordedEvents.get(2).getPitch(), expectedPitch7);
		assertEquals(pm.recordedEvents.get(2).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(2).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(3).getPitch(), expectedPitch7);
		assertEquals(pm.recordedEvents.get(3).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(3).getKind(), expectedEndNote);
		
		assertEquals(pm.recordedEvents.get(4).getPitch(), expectedPitch10);
		assertEquals(pm.recordedEvents.get(4).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(4).getKind(), expectedBeginNote);
		
		assertEquals(pm.recordedEvents.get(5).getPitch(), expectedPitch10);
		assertEquals(pm.recordedEvents.get(5).getInstr(), expectedInstrument);
		assertEquals(pm.recordedEvents.get(5).getKind(), expectedEndNote);
    }
    
    @Test
    public void playbackNoRecordingTest () throws MidiUnavailableException {
    	
    	String expectedBehavior = "";
    	
    	PianoMachine pm = new PianoMachine();
    	
    	Midi midi = Midi.getInstance();
    	midi.clearHistory();
    	
    	assertFalse(pm.isRecording);
    	
    	pm.toggleRecording();
    	assertTrue(pm.isRecording);
    	pm.toggleRecording();
    	assertFalse(pm.isRecording);
    	
    	pm.beginNote(new Pitch(0));
		Midi.wait(100);
		pm.endNote(new Pitch(0));
		
		midi.clearHistory();
		pm.playback();
		assertEquals(expectedBehavior, midi.history());
    }
}
